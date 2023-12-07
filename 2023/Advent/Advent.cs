namespace Advent;

using System.Text.Json;
using Lines = string[];


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

    public string Text()
    {
        return _reader.ReadToEnd().TrimEnd();
    }

    public Lines Lines()
    {
        return Text().Split("\n");
    }

    public void Dispose()
    {
        _reader.Dispose();
    }
}

public static class Debug
{
    public static void Print(object o) => Console.WriteLine(JsonSerializer.Serialize(o));

    public static void Assert(bool assertion)
    {
        System.Diagnostics.Debug.Assert(assertion);
    }

    public static void Assert<T>(T left, T right) where T : IEquatable<T>
    {
        if (left.Equals(right)) return;

        Console.WriteLine($"left: {JsonSerializer.Serialize(left)}");
        Console.WriteLine($"right: {JsonSerializer.Serialize(right)}");
        System.Diagnostics.Debug.Assert(left.Equals(right));
    }
}
