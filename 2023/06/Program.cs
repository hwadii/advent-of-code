using System.Diagnostics;
using System.Text.RegularExpressions;

void Part1()
{
    var contents = Advent.Fs.Open().Lines().ToList();
    var sheet = Sheet.FromRaces(contents);
    Debug.Assert(sheet.Product == 449820);
}

void Part2()
{
    var contents = Advent.Fs.Open().Lines().ToList();
    var sheet = Sheet.FromRace(contents);
    Debug.Assert(sheet.Product == 42250895);
}

Part1();
Part2();

class Sheet
{
    private List<Race> _races;

    public Sheet(List<long> times, List<long> distances)
    {
        _races = times.Zip(distances).Select(dt => new Race() { Time = dt.First, Distance = dt.Second }).ToList();
    }

    public List<Race> Races => _races;

    public long Product => _races.Aggregate(1L, (total, r) => total * r.Count());

    public static Sheet FromRaces(List<string> contents)
    {
        var times = Regex.Replace(contents[0], @"Time:\s*", "").Split(' ', StringSplitOptions.RemoveEmptyEntries).Select(s => long.Parse(s)).ToList();
        var distances = Regex.Replace(contents[1], @"Distance:\s*", "").Split(' ', StringSplitOptions.RemoveEmptyEntries).Select(s => long.Parse(s)).ToList();
        return new Sheet(times, distances);
    }

    public static Sheet FromRace(List<string> contents)
    {
        var time = long.Parse(Regex.Replace(contents[0], @"Time:\s*", "").Replace(" ", ""));
        var distance = long.Parse(Regex.Replace(contents[1], @"Distance:\s*", "").Replace(" ", ""));
        return new Sheet(new() { time }, new() { distance });
    }
}

class Race
{
    public long Time { get; set; }
    public long Distance { get; set; }

    public long Lower()
    {
        for (var held = 0; held < Time; held++)
        {
            var remaining = Time - held;
            var boat = new Boat() { Held = held };
            if (boat.Distance(remaining) > Distance) return boat.Held;
        }
        return default;
    }

    public long Count() => Time - 2 * Lower() + 1;
}

class Boat
{
    public long Held { get; set; }

    public long Distance(long remaining) => Held * remaining;
}
