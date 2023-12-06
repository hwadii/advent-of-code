void Part1()
{
    var alamanac = new Alamanac(Advent.Fs.Open().Lines().ToList());
    Console.WriteLine(alamanac.Location());
}

void Part2()
{
    var alamanac = new Alamanac(Advent.Fs.Open().Lines().ToList());
    Console.WriteLine(alamanac.LocationRange());
}

Part1();
Part2();

class Alamanac
{
    private IEnumerable<long> _seeds;
    private List<Maps> _maps;

    public Alamanac(List<string> contents)
    {
        _seeds = contents[0].Replace("seeds: ", "").Split(" ").Select(s => long.Parse(s));
        _maps = new();
        var current = new Maps();
        foreach (var line in contents[2..])
        {
            if (line.EndsWith(":")) continue;
            else if (line == string.Empty || line.StartsWith(Environment.NewLine))
            {
                _maps.Add(current);
                current = new Maps();
                continue;
            }
            else
            {
                var nums = line.Split(" ");
                current.Add(new Map() { Destination = long.Parse(nums[0]), Source = long.Parse(nums[1]), Length = long.Parse(nums[2]) });
            }
        }
        _maps.Add(current);
    }

    public long LocationRange()
    {
        var ranges = new List<Range>();
        var seeds = _seeds.ToList();
        for (var i = 0; i < seeds.Count - 1; i += 2)
        {
            ranges.Add(new Range(seeds[i], seeds[i] + seeds[i + 1] - 1));
        }
        Advent.Debug.Print(ranges);
        var min = long.MaxValue;
        Parallel.ForEach(
            ranges,
            () => long.MaxValue,
            (range, loop, min) =>
            {
                for (var seed = range.Start; seed <= range.End; seed++)
                {
                    var location = seed;
                    foreach (var map in _maps)
                    {
                        location = map.Next(location);
                    }
                    min = Math.Min(min, location);
                }
                return min;
            },
            result =>
            {
                Interlocked.Exchange(ref min, Math.Min(Interlocked.Read(ref min), result));
            });
        return min;
    }

    public long Location()
    {
        var minLocation = long.MaxValue;
        foreach (var seed in _seeds)
        {
            var location = seed;
            foreach (var map in _maps)
            {
                location = map.Next(location);
            }
            minLocation = Math.Min(minLocation, location);
        }
        return minLocation;
    }

    public List<Maps> Maps => _maps;

    public IEnumerable<long> Seeds => _seeds;
}

class Map
{
    public long Destination { get; set; }
    public long Source { get; set; }
    public long Length { get; set; }

    public long Location(long thing)
    {
        return Source <= thing && thing <= Source + Length ? thing + Math.Abs(Destination - Source) * Sign : thing;
    }

    public long Sign => Source >= Destination ? -1 : 1;
}

class Maps
{
    public List<Map> _maps;

    public Maps()
    {
        _maps = new();
    }

    public List<Map> Value => _maps;

    public void Add(Map map)
    {
        _maps.Add(map);
    }

    public long Next(long start)
    {
        var location = start;
        foreach (var map in _maps)
        {
            if ((location = map.Location(start)) != start) return location;
        }
        return start;
    }
}

record struct Range(long Start, long End);
