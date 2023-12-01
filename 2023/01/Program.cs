var line = string.Empty;
var calibration = 0;
while ((line = Console.ReadLine()) != null)
{
    var l = new Line(line);
    calibration += new Line(line).Calibration();
}
Console.WriteLine(calibration);

class Line
{
    private readonly string _content;
    private readonly static Dictionary<string, int> map = new()
    {
        { "one", 1 },
        { "two", 2 },
        { "three", 3 },
        { "four", 4 },
        { "five", 5 },
        { "six", 6 },
        { "seven", 7 },
        { "eight", 8 },
        { "nine", 9 },
    };

    public Line(string content)
    {
        _content = content;
    }

    public int Calibration()
    {
        var nums = new SortedList<int, int>();
        foreach (var num in map)
        {
            var index = -1;
            if ((index = _content.IndexOf(num.Key)) > -1)
            {
                nums.TryAdd(index, num.Value);
            }
            if ((index = _content.LastIndexOf(num.Key)) > -1)
            {
                nums.TryAdd(index, num.Value);
            }
            if ((index = _content.IndexOf(num.Value.ToString())) > -1)
            {
                nums.TryAdd(index, num.Value);
            }
            if ((index = _content.LastIndexOf(num.Value.ToString())) > -1)
            {
                nums.TryAdd(index, num.Value);
            }
        }
        return int.Parse($"{nums.First().Value}{nums.Last().Value}");
    }
}
