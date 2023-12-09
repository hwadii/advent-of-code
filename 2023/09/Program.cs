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
    private Sequence _nums;

    public History(string nums)
    {
        _nums = new Sequence(nums.Split(' ').Select(n => int.Parse(n)).ToList());
    }

    public int Next()
    {
        var next = _nums.Last;

        while (!_nums.Zeroes)
        {
            _nums = _nums.Next();
            next += _nums.Last;
        }

        return next;
    }

    public Sequence Nums => _nums;
}

class Sequence
{
    private readonly List<int> _nums;

    public Sequence(List<int> nums)
    {
        _nums = nums;
    }

    public Sequence Next()
    {
        var nums = new List<int>();

        for (var i = 0; i < _nums.Count - 1; i++)
        {
            nums.Add(_nums[i + 1] - _nums[i]);
        }

        return new Sequence(nums);
    }

    public int First => _nums.First();

    public int Last => _nums.Last();

    public bool Zeroes => _nums.All(n => n == 0);

    public List<int> Nums => _nums;
}
