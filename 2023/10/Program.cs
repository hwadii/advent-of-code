using Position = (int X, int Y);

void Part1()
{
    var contents = Advent.Fs.Open().Lines();
    var maze = new Maze(contents);
    Advent.Debug.Assert(maze.FloodFill().Count / 2, 6649);
}

void Part2()
{
    var contents = Advent.Fs.Open().Lines();
    var maze = new Maze(contents);
    Advent.Debug.Assert(maze.Enclosed(), 601);
}

Part1();
Part2();

class Maze
{
    private readonly List<Tile> _tiles = new();
    private readonly Position _dimensions;
    private readonly Tile _start = default!;

    public Maze(string[] contents)
    {
        _dimensions = (contents[0].Length, contents.Length);
        for (var j = 0; j < contents.Length; j++)
        {
            for (var i = 0; i < contents[j].Length; i++)
            {
                var tile = new Tile(contents[j][i], (i, j));
                if (tile.IsStart) _start = tile;
                _tiles.Add(tile);
            }
        }
    }

    public HashSet<Tile> FloodFill()
    {
        var stack = new Stack<Tile>(new List<Tile>() { _start });
        var visited = new HashSet<Tile>() { _start };
        while (stack.Count != 0)
        {
            var first = stack.Pop();
            var west = Get(first.Position.X - 1, first.Position.Y);
            if (!visited.Contains(west) && first.IsValid(west, Tile.Direction.West)) stack.Push(west);
            var east = Get(first.Position.X + 1, first.Position.Y);
            if (!visited.Contains(east) && first.IsValid(east, Tile.Direction.East)) stack.Push(east);
            var north = Get(first.Position.X, first.Position.Y - 1);
            if (!visited.Contains(north) && first.IsValid(north, Tile.Direction.North)) stack.Push(north);
            var south = Get(first.Position.X, first.Position.Y + 1);
            if (!visited.Contains(south) && first.IsValid(south, Tile.Direction.South)) stack.Push(south);
            visited.Add(first);
        }
        return visited;
    }

    public int Enclosed()
    {
        var visited = FloodFill();
        var enclosed = 0;
        var minX = visited.MinBy(t => t.Position.X)!.Position.X;
        var minY = visited.MinBy(t => t.Position.Y)!.Position.Y;
        var maxX = visited.MaxBy(t => t.Position.X)!.Position.X;
        var maxY = visited.MaxBy(t => t.Position.Y)!.Position.Y;
        for (var i = minX; i <= maxX; i++)
        {
            for (var j = minY; j <= maxY; j++)
            {
                var tile = Get(i, j);
                if (visited.Contains(Get(i, j))) continue;
                var counter = 0;
                var x = tile.Position.X;
                foreach (var _ in Enumerable.Range(0, maxX))
                {
                    x -= 1;
                    if (x < 0) break;
                    var t = Get(x, tile.Position.Y);
                    counter += visited.Contains(t) && t.IsWall ? 1 : 0;
                }
                enclosed += counter % 2;
            }
        }
        return enclosed;
    }

    public Tile Get(int x, int y)
    {
        if (0 > x || x >= _dimensions.X) return default!;
        if (0 > y || y >= _dimensions.Y) return default!;
        return _tiles[y * _dimensions.X + x];
    }

    public List<Tile> Tiles => _tiles;

    public Tile Start => _start;

    public Position Dimensions => _dimensions;
}

class Tile
{
    private readonly char _type;
    private readonly Position _position;

    public enum Direction
    {
        North,
        South,
        West,
        East,
    }

    public static Dictionary<char, Dictionary<Direction, string>> Connections = new()
    {
        {
            'S',
            new()
            {
                { Direction.North, "|7F" },
                { Direction.South, "|LJ" },
                { Direction.East, "-J7" },
                { Direction.West, "-FL" }
            }
        },
        {
            '.',
            new()
            {
                { Direction.North, string.Empty },
                { Direction.South, string.Empty },
                { Direction.East, string.Empty },
                { Direction.West, string.Empty }
            }
        },
        {
            '|',
            new()
            {
                { Direction.North, "|7F" },
                { Direction.South, "|LJ" },
                { Direction.East, string.Empty },
                { Direction.West, string.Empty }
            }
        },
        {
            '-',
            new()
            {
                { Direction.North, string.Empty },
                { Direction.South, string.Empty },
                { Direction.East, "-J7" },
                { Direction.West, "-FL" }
            }
        },
        {
            'L',
            new()
            {
                { Direction.North, "|7F" },
                { Direction.South, string.Empty },
                { Direction.East, "-J7" },
                { Direction.West, string.Empty }
            }
        },
        {
            'J',
            new()
            {
                { Direction.North, "|7F" },
                { Direction.South, string.Empty },
                { Direction.East, string.Empty },
                { Direction.West, "-FL" }
            }
        },
        {
            '7',
            new()
            {
                { Direction.North, string.Empty },
                { Direction.South, "|LJ" },
                { Direction.East, string.Empty },
                { Direction.West, "-FL" }
            }
        },
        {
            'F',
            new()
            {
                { Direction.North, string.Empty },
                { Direction.South, "|LJ" },
                { Direction.East, "-J7" },
                { Direction.West, string.Empty }
            }
        },
    };

    public Tile(char type, Position position)
    {
        _type = type;
        _position = position;
    }

    internal bool IsStart => _type == 'S';

    internal bool IsWall => "|JLS".Contains(_type);

    public char Type => _type;

    public bool IsValid(Tile? other, Direction direction)
    {
        if (other == null) return false;
        return Connections[_type][direction].Contains(other.Type);
    }

    public Position Position => _position;
}
