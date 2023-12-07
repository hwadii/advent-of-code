void Part1()
{
    var contents = Advent.Fs.Open().Lines();
    var game = new Game(contents, Card.CompareType);
    Advent.Debug.Assert(game.Winnings(), 250957639);
}

void Part2()
{
    var contents = Advent.Fs.Open().Lines();
    var game = new Game(contents, Card.CompareBestType);
    Advent.Debug.Assert(game.Winnings(), 251515496);
}

Part1();
Part2();

class Game
{
    private List<Card> _cards = new();

    public Game(string[] contents, Comparison<Card> comparer)
    {
        foreach (var hand in contents)
        {
            var content = hand.Split(' ');
            _cards.Add(new Card(content[0], int.Parse(content[1])));
        }
        _cards.Sort(comparer);
    }

    public int Winnings()
    {
        return _cards
            .Select((value, i) => (i, value))
            .Aggregate(0, (total, card) => total + (card.i + 1) * card.value.Bid);
    }

    public List<Card> Cards { get => _cards; set => _cards = value; }
}

class Card
{
    private readonly string _hand;
    private readonly int _bid;
    private readonly Dictionary<char, int> _tally;

    public int Bid => _bid;

    public string Hand => _hand;

    public enum CardType
    {
        HighCard,
        OnePair,
        TwoPair,
        ThreeOfKind,
        FullHouse,
        FourOfKind,
        FiveOfKind,
    }

    public static string Order = "23456789TJQKA";
    public static string JokerOrder = "J23456789TQKA";

    public Card(string hand, int bid)
    {
        _hand = hand;
        _bid = bid;
        _tally = Tally();
    }

    public CardType Type()
    {
        var distincts = _hand.Distinct().Count();
        if (distincts == 1) return CardType.FiveOfKind;
        if (distincts == 5) return CardType.HighCard;
        if (distincts == 4) return CardType.OnePair;
        var max = _tally.Max(g => g.Value);
        if (max == 4) return CardType.FourOfKind;
        if (_tally.Count(g => g.Value == 3) == 1 && _tally.Count(g => g.Value == 2) == 1) return CardType.FullHouse;
        return max == 3 ? CardType.ThreeOfKind : CardType.TwoPair;
    }

    public CardType BestType()
    {
        var type = Type();
        if (type is CardType.HighCard)
        {
            if (_tally.GetValueOrDefault('J') == 1) return CardType.OnePair;
            else return CardType.HighCard;
        }
        else if (type is CardType.OnePair)
        {
            if (_tally.GetValueOrDefault('J') == 2) return CardType.ThreeOfKind;
            else if (_tally.GetValueOrDefault('J') == 1) return CardType.ThreeOfKind;
            else return CardType.OnePair;
        }
        else if (type is CardType.TwoPair)
        {
            if (_tally.GetValueOrDefault('J') == 2) return CardType.FourOfKind;
            else if (_tally.GetValueOrDefault('J') == 1) return CardType.FullHouse;
            else return CardType.TwoPair;
        }
        else if (type is CardType.ThreeOfKind)
        {
            if (_tally.GetValueOrDefault('J') == 3) return CardType.FourOfKind;
            else if (_tally.GetValueOrDefault('J') == 1) return CardType.FourOfKind;
            else return CardType.ThreeOfKind;
        }
        else if (type is CardType.FullHouse)
        {
            if (_tally.GetValueOrDefault('J') == 3) return CardType.FiveOfKind;
            else if (_tally.GetValueOrDefault('J') == 2) return CardType.FiveOfKind;
            else return CardType.FullHouse;
        }
        else if (type is CardType.FourOfKind)
        {
            if (_tally.GetValueOrDefault('J') == 4) return CardType.FiveOfKind;
            else if (_tally.GetValueOrDefault('J') == 1) return CardType.FiveOfKind;
            else return CardType.FourOfKind;
        }
        return CardType.FiveOfKind;
    }

    public Dictionary<char, int> Tally()
    {
        var tally = new Dictionary<char, int>();
        foreach (var c in _hand)
        {
            if (tally.GetValueOrDefault(c) == default)
            {
                tally[c] = 1;
            }
            else
            {
                tally[c] += 1;
            }
        }
        return tally;
    }

    public static int CompareType(Card card, Card other)
    {
        var comparison = card.Type().CompareTo(other.Type());
        if (comparison == 0)
        {
            foreach (var value in card.Hand.Zip(other.Hand))
            {
                if (Order.IndexOf(value.First) > Order.IndexOf(value.Second)) return 1;
                if (Order.IndexOf(value.First) < Order.IndexOf(value.Second)) return -1;
            }
            return 0;
        }
        else
        {
            return comparison;
        }
    }

    public static int CompareBestType(Card card, Card other)
    {
        var comparison = card.BestType().CompareTo(other.BestType());
        if (comparison == 0)
        {
            foreach (var value in card.Hand.Zip(other.Hand))
            {
                if (JokerOrder.IndexOf(value.First) > JokerOrder.IndexOf(value.Second)) return 1;
                if (JokerOrder.IndexOf(value.First) < JokerOrder.IndexOf(value.Second)) return -1;
            }
            return 0;
        }
        else
        {
            return comparison;
        }
    }
}
