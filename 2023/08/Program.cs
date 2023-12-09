using Nodes = System.Collections.Generic.Dictionary<string, Node>;

void Part1()
{
    var contents = Advent.Fs.Open().Lines();
    var network = new Network(contents);
    Advent.Debug.Assert(network.Steps(), 13301UL);
}

void Part2()
{
    var contents = Advent.Fs.Open().Lines();
    var network = new Network(contents);
    Advent.Debug.Assert(network.StepsMultiple(), 7309459565207UL);
}

Part1();
Part2();

class Network
{
    private readonly string _directions;
    private readonly Nodes _nodes = new();

    public Network(string[] contents)
    {
        _directions = contents[0];
        foreach (var node in contents[2..])
        {
            var content = node.Split(" = ");
            var value = content[0];
            var possible = content[1].Replace("(", "").Replace(")", "").Split(", ");
            _nodes.Add(value, new Node(value) { Left = possible[0], Right = possible[1] });
        }
    }

    public ulong Steps() => new Path(_nodes.First(n => n.Value.Value == "AAA").Value).Steps(_nodes, _directions);

    public ulong StepsMultiple()
    {
        var starts = _nodes.Where(n => n.Value.Value.EndsWith('A')).Select(n => n.Value).ToList();
        return starts
            .Select(n => new Path(n).Steps(_nodes, _directions))
            .Aggregate(1UL, (lcm, l) => Lcm(lcm, l));
    }

    public static ulong Gcd(ulong a, ulong b)
    {
        while (a != b)
        {
            var c = Math.Min(a, b);
            if (a > b) a = a - b;
            else a = b - a;
            b = c;
        }
        return a;
    }

    public static ulong Lcm(ulong a, ulong b) => a * b / Gcd(a, b);

    public Nodes Nodes => _nodes;
}

class Path
{
    private readonly Node _start;

    public Path(Node start)
    {
        _start = start;
    }

    public ulong Steps(Nodes nodes, string directions)
    {
        var node = _start;
        var steps = 0UL;
        var i = 0;
        while (!node.IsEnd)
        {
            node = directions[i] switch
            {
                'L' => nodes[node.Left],
                'R' => nodes[node.Right],
                _ => throw new ArgumentOutOfRangeException(),
            };
            steps += 1;
            i = (i + 1) % directions.Length;
        }
        return steps;
    }
}

class Node
{
    private readonly string _value;
    public string Left { get; set; } = string.Empty;
    public string Right { get; set; } = string.Empty;

    public Node(string value)
    {
        _value = value;
    }

    public bool IsStart => _value == "AAA";

    public bool IsEnd => _value.EndsWith("Z");

    public string Value => _value;
}
