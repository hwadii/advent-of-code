void Part1()
{
    var contents = Advent.Fs.Open().Lines();
    Console.WriteLine(new Game(contents).Points());
}

void Part2()
{
    var contents = Advent.Fs.Open().Lines();
    Console.WriteLine(new Game(contents).Copies());
}

Part1();
Part2();

class Game
{
    private Dictionary<int, Card> _cards = new();

    public Game(IEnumerable<string> games)
    {
        foreach (var game in games)
        {
            var card = new Card(game);
            _cards.Add(card.Id, card);
        }
    }

    public int Copies()
    {
        var wins = new Dictionary<int, IEnumerable<int>>();
        var counts = new Dictionary<int, int>();

        foreach (var card in _cards)
        {
            wins.Add(card.Key, card.Value.Wins());
            counts.Add(card.Key, 1);
        }

        foreach (var win in wins)
        {
            foreach (var card in win.Value)
            {
                counts[card] += counts[win.Key];
            }
        }

        return counts.Values.Sum();
    }

    public double Points()
    {
        return _cards.Sum(c => c.Value.Points());
    }
}

class Card
{
    private readonly int _id;
    private readonly string[] _numbers;

    public Card(string contents)
    {
        var parts = contents.Split(": ");
        _id = int.Parse(parts[0].Replace("Card ", "").TrimStart());
        _numbers = parts[1].Split(" | ");
    }

    public int Id => _id;

    public IEnumerable<int> Wins()
    {
        var counts = WinningNumbers().Intersect(DrawnNumbers()).Count();
        return Enumerable.Range(Id + 1, counts);
    }

    public double Points()
    {
        var counts = WinningNumbers().Intersect(DrawnNumbers()).Count();
        return counts == 0 ? 0 : Math.Pow(2, counts - 1);
    }

    public IEnumerable<int> WinningNumbers()
    {
        return _numbers.First()
            .Split(" ")
            .Where(w => w != string.Empty)
            .Select(w => int.Parse(w.Trim()));
    }

    public IEnumerable<int> DrawnNumbers()
    {
        return _numbers[1]
            .Split(" ")
            .Where(w => w != string.Empty)
            .Select(w => int.Parse(w.Trim()));
    }
}
