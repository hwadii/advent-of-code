const std = @import("std");
const print = std.debug.print;

fn sliceFromFile(alloc: std.mem.Allocator, path: []const u8) ![]u8 {
    const flags = std.fs.File.OpenFlags{};
    const file = try std.fs.cwd().openFile(path, flags);
    defer file.close();
    const slice = try file.reader().readAllAlloc(alloc, (try file.stat()).size);
    return slice;
}

pub fn getMaxCalories(in: []u8) u32 {
    var parts = std.mem.split(u8, in, "\n\n");
    var max: u32 = 0;
    while (parts.next()) |part| {
        var smaller_parts = std.mem.split(u8, part, "\n");
        var running: u32 = 0;
        while (smaller_parts.next()) |smaller| {
            running += std.fmt.parseUnsigned(u32, smaller, 10) catch 0;
        }
        if (running >= max) {
            max = running;
        }
    }
    return max;
}

pub fn getTopThree(alloc: std.mem.Allocator, in: []u8) !u32 {
    var sums = std.ArrayList(u32).init(alloc);
    var parts = std.mem.split(u8, in, "\n\n");
    while (parts.next()) |part| {
        var smaller_parts = std.mem.split(u8, part, "\n");
        var running: u32 = 0;
        while (smaller_parts.next()) |smaller| {
            running += std.fmt.parseUnsigned(u32, smaller, 10) catch 0;
        }
        try sums.append(running);
    }
    var data: []u32 = sums.toOwnedSlice();
    defer alloc.free(data);
    var sum: u32 = 0;
    std.sort.sort(u32, data, {}, comptime std.sort.desc(u32));
    for (data[0..3]) |value| {
        sum += value;
    }
    return sum;
}

test "getMaxCalories" {
    const first_slice = try sliceFromFile(std.testing.allocator, "./sample.txt");
    try std.testing.expectEqual(getMaxCalories(first_slice), 24000);
    const second_slice = try sliceFromFile(std.testing.allocator, "./input.txt");
    try std.testing.expectEqual(getMaxCalories(second_slice), 66616);
    defer {
        std.testing.allocator.free(first_slice);
        std.testing.allocator.free(second_slice);
    }
}

test "getTopThree" {
    const first_slice = try sliceFromFile(std.testing.allocator, "./sample.txt");
    try std.testing.expectEqual(try getTopThree(std.testing.allocator, first_slice), 45000);
    const second_slice = try sliceFromFile(std.testing.allocator, "./input.txt");
    try std.testing.expectEqual(try getTopThree(std.testing.allocator, second_slice), 199172);
    defer {
        std.testing.allocator.free(first_slice);
        std.testing.allocator.free(second_slice);
    }
}
