using Position = (int X, int Y);

void Part1()
{
    var parts = new List<Part>();
    var schematic = new Schematic();
    Console.WriteLine(schematic.Sum());
}

void Part2()
{
    var parts = new List<Part>();
    var schematic = new Schematic();
    Console.WriteLine(schematic.GearRatio());
}

Part1();
Part2();

class Schematic
{
    private readonly List<string> _lines;

    public Schematic()
    {
        _lines = Advent.Fs.Open().Lines().ToList();
    }

    public int Sum()
    {
        var symbols = Symbols(c => !char.IsDigit(c) && c != '.');
        var partsSum = 0;
        var parts = Parts();
        foreach (var part in parts)
        {
            foreach (var symbol in symbols)
            {
                if (part.IsAdjacent(symbol)) partsSum += int.Parse(part.Number);
            }
        }
        return partsSum;
    }

    public int GearRatio()
    {
        var symbols = Gears();
        var gearRatio = 0;
        var parts = Parts();
        foreach (var gear in symbols)
        {
            var seen = 0;
            var product = 1;
            foreach (var part in parts)
            {
                if (part.IsAdjacent(gear))
                {
                    seen += 1;
                    product *= int.Parse(part.Number);
                }
            }
            if (seen == 2) gearRatio += product;
        }
        return gearRatio;
    }

    public List<Part> Parts()
    {
        var parts = new List<Part>();
        var currentPart = default(Part);
        for (var j = 0; j < _lines.Count; j++)
        {
            var chars = _lines[j].ToCharArray();
            for (var i = 0; i < chars.Count(); i++)
            {
                if (char.IsDigit(chars[i]))
                {
                    if (string.IsNullOrEmpty(currentPart.Number)) currentPart.Number = string.Empty;
                    currentPart.Number += chars[i];
                    if (currentPart.Positions == null) currentPart.Positions = new();
                    currentPart.Positions.Add((i, j));
                }
                else if (currentPart != default)
                {
                    parts.Add(currentPart);
                    currentPart = default;
                }
            }
        }
        return parts;
    }

    public List<Symbol> Gears()
    {
        return Symbols(c => c == '*');
    }

    public List<Symbol> Symbols(Func<char, bool> predicate)
    {
        var symbols = new List<Symbol>();
        for (var j = 0; j < _lines.Count; j++)
        {
            var chars = _lines[j].ToCharArray();
            for (var i = 0; i < chars.Count(); i++)
            {
                if (predicate(chars[i]))
                {
                    symbols.Add(new Symbol(Value: chars[i], Position: (i, j)));
                }
            }
        }
        return symbols;
    }
}

record struct Symbol(char Value, Position Position);

record struct Part(string Number, List<Position> Positions)
{
    public bool IsAdjacent(Symbol symbol)
    {
        foreach (var position in Positions)
        {
            for (var j = symbol.Position.Y - 1; j <= symbol.Position.Y + 1; j++)
            {
                for (var i = symbol.Position.X - 1; i <= symbol.Position.X + 1; i++)
                {
                    if (position == (i, j)) return true;
                }
            }
        }
        return false;
    }
}
