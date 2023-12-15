void Part1()
{
    var contents = Advent.Fs.Open().Lines().First().Split(",");
    var hash = 0;
    foreach (var message in contents)
    {
        hash += new Lens(message).Hash();
    }
    Console.WriteLine(hash);
}

void Part2()
{
    var contents = Advent.Fs.Open().Lines().First().Split(",");
    var power = 0L;
    var boxes = new Dictionary<int, Box>();
    foreach (var message in contents)
    {
        var lens = new Lens(message);
        var hash = lens.HashLabel();
        var box = boxes.GetValueOrDefault(hash);
        if (box == default) boxes[hash] = new(hash);
        if (lens.Op == Lens.Operation.Equal)
        {
            boxes[hash].Add(lens);
        } else
        {
            boxes[hash].Remove(lens);
        }
    }
    foreach (var box in boxes.Values)
    {
        power += box.FocusingPower();
    }
    Advent.Debug.Print(power);
}

Part2();

class Box
{
    private readonly List<Lens> _lenses = new();
    private readonly int _id;

    public Box(int id)
    {
        _id = id;
    }

    public List<Lens> Lenses => _lenses;

    public void Add(Lens lens)
    {
        var index = _lenses.FindIndex(l => l.Label == lens.Label);
        if (index == -1) _lenses.Add(lens);
        else
        {
            _lenses[index] = lens;
        }
    }

    public void Remove(Lens lens)
    {
        var index = _lenses.FindIndex(l => l.Label == lens.Label);
        if (index > -1) _lenses.RemoveAt(index);
    }

    public long FocusingPower()
    {
        var power = 0L;
        for (var i = 0; i < _lenses.Count; i++)
        {
            var lens = _lenses[i];
            power += (_id + 1) * lens.FocalLength * (i + 1);
        }
        return power;
    }
}

class Lens
{
    private readonly string _content;
    private readonly string _label;

    public enum Operation
    {
        Dash,
        Equal
    }

    public Lens(string content)
    {
        _content = content;
        _label = string.Concat(content.TakeWhile(c => c != '=' && c != '-'));
    }

    public string Content => _content;

    public int Hash()
    {
        int start = 0;
        foreach (var c in _content)
        {
            start += (int)c;
            start *= 17;
            start %= 256;
        }
        return start;
    }

    public int HashLabel()
    {
        int hash = 0;
        foreach (var c in _label)
        {
            hash += (int)c;
            hash *= 17;
            hash %= 256;
        }
        return hash;
    }

    public int FocalLength => Op == Lens.Operation.Equal ? int.Parse(_content.Last().ToString()) : default;

    public int LabelHash => HashLabel();

    public Operation Op => _content.Contains("=") ? Lens.Operation.Equal : Lens.Operation.Dash;

    public string Label => _label;
}
