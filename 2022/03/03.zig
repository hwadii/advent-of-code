const std = @import("std");
const print = std.debug.print;
const assert = std.debug.assert;

const Item = struct {
    char: u8,
    value: u8,

    pub fn fromChar(c: u8) Item {
        return switch (c) {
            'a'...'z' => Item{ .char = c, .value = c - 96 },
            'A'...'Z' => Item{ .char = c, .value = c - 38 },
            else => unreachable,
        };
    }
};

const Rumsack = struct {
    pub fn fromPart(items: *std.AutoHashMap(u8, void), part: []const u8) !void {
        var i: usize = 0;
        while (i < part.len) : (i += 1) {
            try items.put(part[i], {});
        }
    }
};

pub fn part1(alloc: std.mem.Allocator, input: []const u8) !u32 {
    var parts = std.mem.split(u8, input, "\n");
    var map = std.AutoHashMap(u8, void).init(alloc);
    var running_total: u32 = 0;
    defer map.deinit();
    while (parts.next()) |part| {
        if (std.mem.eql(u8, part, "")) {
            break;
        }
        const middle = part.len / 2;
        var first_rucksack = part[0..middle];
        var second_rucksack = part[middle..];
        assert(first_rucksack.len == second_rucksack.len);
        try Rumsack.fromPart(&map, first_rucksack);
        var i: usize = 0;
        while (i < second_rucksack.len) : (i += 1) {
            if (map.getEntry(second_rucksack[i]) != null) {
                running_total += Item.fromChar(second_rucksack[i]).value;
                break;
            }
        }
        map.clearRetainingCapacity();
    }
    return running_total;
}

pub fn part2(alloc: std.mem.Allocator, input: []const u8) !u32 {
    var parts = std.mem.split(u8, input, "\n");
    var first_rumsack = std.AutoHashMap(u8, void).init(alloc);
    var second_rumsack = std.AutoHashMap(u8, void).init(alloc);
    defer {
        first_rumsack.deinit();
        second_rumsack.deinit();
    }
    var running_total: u32 = 0;
    var lineno: usize = 0;
    while (parts.next()) |part| {
        if (std.mem.eql(u8, part, "")) {
            break;
        }
        if ((lineno + 1) % 3 == 1) {
            try Rumsack.fromPart(&first_rumsack, part);
        } else if ((lineno + 1) % 3 == 2) {
            try Rumsack.fromPart(&second_rumsack, part);
        } else {
            const item = blk: {
                var i: usize = 0;
                while (i < part.len) : (i += 1) {
                    if (first_rumsack.getEntry(part[i]) != null and second_rumsack.getEntry(part[i]) != null) {
                        break :blk Item.fromChar(part[i]);
                    }
                }
                unreachable;
            };
            running_total += item.value;
            first_rumsack.clearRetainingCapacity();
            second_rumsack.clearRetainingCapacity();
        }
        lineno += 1;
    }
    return running_total;
}

test "part1" {
    const sample = @embedFile("sample.txt");
    try std.testing.expectEqual(try part1(std.testing.allocator, sample), 157);
    const input = @embedFile("input.txt");
    try std.testing.expectEqual(try part1(std.testing.allocator, input), 7428);
}

test "part2" {
    const sample = @embedFile("./sample.txt");
    try std.testing.expectEqual(try part2(std.testing.allocator, sample), 70);
    const input = @embedFile("./input.txt");
    try std.testing.expectEqual(try part2(std.testing.allocator, input), 2650);
}
