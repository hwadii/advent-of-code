using System.Text.RegularExpressions;

void Part1()
{
    var contents = Advent.Fs.Open().Lines().ToList();
    var sheet = Sheet.FromRaces(contents);
    Advent.Debug.Assert(sheet.Product, 449820);
}

void Part2()
{
    var contents = Advent.Fs.Open().Lines().ToList();
    var sheet = Sheet.FromRace(contents);
    Advent.Debug.Assert(sheet.Product, 42250895);
}

Part1();
Part2();

class Sheet
{
    private List<Race> _races;

    public Sheet(List<ulong> times, List<ulong> distances)
    {
        _races = times.Zip(distances).Select(dt => new Race() { Time = dt.First, Distance = dt.Second }).ToList();
    }

    public List<Race> Races => _races;

    public double Product => _races.Aggregate(1UL, (total, r) => total * r.Count());

    public static Sheet FromRaces(List<string> contents)
    {
        var times = Regex.Replace(contents[0], @"Time:\s*", "").Split(' ', StringSplitOptions.RemoveEmptyEntries).Select(s => ulong.Parse(s)).ToList();
        var distances = Regex.Replace(contents[1], @"Distance:\s*", "").Split(' ', StringSplitOptions.RemoveEmptyEntries).Select(s => ulong.Parse(s)).ToList();
        return new Sheet(times, distances);
    }

    public static Sheet FromRace(List<string> contents)
    {
        var time = ulong.Parse(Regex.Replace(contents[0], @"Time:\s*", "").Replace(" ", ""));
        var distance = ulong.Parse(Regex.Replace(contents[1], @"Distance:\s*", "").Replace(" ", ""));
        return new Sheet(new() { time }, new() { distance });
    }
}

class Race
{
    public ulong Time { get; set; }
    public ulong Distance { get; set; }

    public ulong Count()
    {
        var discriminant = Math.Pow(Time, 2) - 4 * Distance;
        var lo = (Time - Math.Sqrt(discriminant)) / 2;
        var hi = (Time + Math.Sqrt(discriminant)) / 2;
        if (lo % 1 == 0) lo += 1;
        if (hi % 1 == 0) hi -= 1;
        return (ulong)(Math.Floor(hi) - Math.Ceiling(lo) + 1);
    }
}
