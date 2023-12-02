using System.Text.RegularExpressions;

void Part1()
{
    var line = string.Empty;
    var id = 1;
    var possible = 0;
    while ((line = Console.ReadLine()) != null)
    {
        possible += new Game(line).IsPossible() ? id : 0;
        id += 1;
    }
    Console.WriteLine(possible);
}

void Part2()
{
    var line = string.Empty;
    var id = 1;
    var power = 0;
    while ((line = Console.ReadLine()) != null)
    {
        power += new Game(line).Sum();
        id += 1;
    }
    Console.WriteLine(power);
}

Part1();

class Game
{
    private static Cubes Configuration = new() { Red = 12, Green = 13, Blue = 14 };
    private string _line = string.Empty;

    public Game(string line)
    {
        _line = Regex.Replace(line, @"Game \d+: ", "");
    }

    public IEnumerable<Cubes> Cubes()
    {
        var records = _line.Split("; ");
        var cubes = new List<Cubes>();
        return records.Select(record =>
        {
            var red = 0;
            var green = 0;
            var blue = 0;
            int.TryParse(Regex.Match(record, @"(\d+) red").Groups[1].Value, out red);
            int.TryParse(Regex.Match(record, @"(\d+) green").Groups[1].Value, out green);
            int.TryParse(Regex.Match(record, @"(\d+) blue").Groups[1].Value, out blue);
            return new Cubes { Red = red, Green = green, Blue = blue };
        });
    }

    public bool IsPossible()
    {
        foreach (var cube in Cubes())
        {
            if (!cube.IsCompatible(Configuration))
            {
                return false;
            }
        }
        return true;
    }

    public int Sum()
    {
        var min = new Cubes() { Red = int.MinValue, Green = int.MinValue, Blue = int.MinValue };
        foreach (var cube in Cubes())
        {
            if (cube.Red != 0) min.Red = Math.Max(cube.Red, min.Red);
            if (cube.Green != 0) min.Green = Math.Max(cube.Green, min.Green);
            if (cube.Blue != 0) min.Blue = Math.Max(cube.Blue, min.Blue);
        }
        return min.Power();
    }
}

record struct Cubes(int Red, int Green, int Blue)
{
    public bool IsCompatible(Cubes other) => Red <= other.Red && Green <= other.Green && Blue <= other.Blue;

    public int Power() => Red * Green * Blue;
}
