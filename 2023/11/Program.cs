using Position = (long X, long Y);

void Part1()
{
    var contents = Advent.Fs.Open().Lines();
    Advent.Debug.Assert(new Image(Image.Expand(contents).ToArray()).Sum(), 10490062);
}

void Part2()
{
    var contents = Advent.Fs.Open().Lines();
    Advent.Debug.Assert(new Image(Image.ExpandFarther(contents).ToArray()).Sum(), 382979724122);
}

Part1();
Part2();

class Image
{
    private readonly List<Galaxy> _galaxies = new();
    private readonly Position _dimensions;

    public Image(string[] contents)
    {
        _dimensions = (contents[0].Length, contents.Length);

        var warpY = 0;
        var size = 1_000_000;
        for (var j = 0; j < contents.Length; j++)
        {
            if (contents[j][0] == '|')
            {
                warpY += size - 1;
                continue;
            }
            var warpX = 0;
            for (var i = 0; i < contents[j].Length; i++)
            {
                switch (contents[j][i])
                {
                    case '#':
                        _galaxies.Add(new Galaxy((i + warpX, j + warpY)));
                        break;
                    case '|':
                        warpX += size - 1;
                        break;
                }
            }
        }
    }

    public List<(Galaxy, Galaxy)> Combinations()
    {
        var combinations = new List<(Galaxy, Galaxy)>();
        for (var i = 0; i < _galaxies.Count; i++)
        {
            for (var j = i + 1; j < _galaxies.Count; j++)
            {
                combinations.Add((_galaxies[i], _galaxies[j]));
            }
        }
        return combinations;
    }

    public long Sum()
    {
        var sum = 0L;
        foreach (var (source, destination) in Combinations())
        {
            var distance = source.Distance(destination);
            sum += distance;
        }
        return sum;
    }

    public static List<string> ExpandFarther(string[] contents)
    {
        var expanded = new List<string>();
        var indexes = new List<int>();
        for (var i = 0; i < contents[0].Length; i++)
        {
            for (var j = 0; j < contents.Length; j++)
            {
                if (contents[j][i] == '#') break;
                if (j == contents.Length - 1) indexes.Add(i);
            }
        }

        for (var i = 0; i < contents.Length; i++)
        {
            foreach (var idx in indexes)
            {
                var chars = contents[i].ToCharArray();
                chars[idx] = '|';
                contents[i] = new string(chars);
            }
        }

        foreach (var line in contents)
        {
            if (line.All(c => c == '.' || c == '|'))
            {
                expanded.Add(new string('|', contents[0].Length));
            }
            else
            {
                expanded.Add(line);
            }
        }
        return expanded;
    }

    public static List<string> Expand(string[] contents)
    {
        var expanded = new List<string>();
        var indexes = new List<int>();
        for (var i = 0; i < contents[0].Length; i++)
        {
            for (var j = 0; j < contents.Length; j++)
            {
                if (contents[j][i] == '#') break;
                if (j == contents.Length - 1) indexes.Add(i);
            }
        }

        for (var i = 0; i < contents.Length; i++)
        {
            var pad = 0;
            foreach (var idx in indexes)
            {
                contents[i] = contents[i].Insert(idx + pad, ".");
                pad += 1;
            }
        }

        foreach (var line in contents)
        {
            if (line.All(c => c == '.'))
            {
                expanded.Add(line);
            }
            expanded.Add(line);
        }
        return expanded;
    }

    public List<Galaxy> Galaxies => _galaxies;
}

class Galaxy
{
    private readonly Position _position;

    public Position Position => _position;

    public Galaxy(Position position)
    {
        _position = position;
    }

    public long Distance(Galaxy other)
    {
        return Math.Abs(other.Position.X - Position.X) + Math.Abs(other.Position.Y - Position.Y);
    }

    public override string ToString()
    {
        return $"Galaxy ({_position.X}, {_position.Y})";
    }
}
