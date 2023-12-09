void Part1()
{
    var contents = Advent.Fs.Open().Lines();
    var sum = contents.Sum(c => new History(c).Next());
    Advent.Debug.Assert(sum, 2101499000);
}

void Part2()
{
    var contents = Advent.Fs.Open().Lines();
    var sum = contents.Sum(c => new History(string.Join(' ', Enumerable.Reverse(c.Split(' ')))).Next());
    Advent.Debug.Assert(sum, 1089);
}

Part1();
Part2();

class History
{
    private List<int> _nums;

    public History(string nums)
    {
        _nums = nums.Split(' ').Select(n => int.Parse(n)).ToList();
    }

    public int Next()
    {
        var next = _nums.Last();

        while (!_nums.All(n => n == 0))
        {
            var nums = new List<int>();

            for (var i = 0; i < _nums.Count - 1; i++)
            {
                nums.Add(_nums[i + 1] - _nums[i]);
            }

            next += nums.Last();
            _nums = nums;
        }

        return next;
    }

    public List<int> Nums => _nums;
}
