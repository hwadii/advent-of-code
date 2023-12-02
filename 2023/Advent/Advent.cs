namespace Advent;

using Lines = IEnumerable<string>;

public static class Fs
{
    public static File Open(string path) => new File(path);
    public static File Open() => new File(Environment.GetCommandLineArgs()[1]);
}

public class File : IDisposable
{
    private readonly System.IO.StreamReader _reader;

    public File(string path)
    {
        _reader = System.IO.File.OpenText(path);
    }

    public Lines Lines()
    {
        return _reader.ReadToEnd().TrimEnd().Split("\n");

    }

    public void Dispose()
    {
      _reader.Dispose();
    }
}
