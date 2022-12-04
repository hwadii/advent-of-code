const std = @import("std");
const print = std.debug.print;

const Section = struct {
    start: u8,
    end: u8,

    fn contains(self: Section, id: u8) bool {
        return self.start <= id and id <= self.end;
    }

    fn covers(self: Section, other: Section) bool {
        return self.start <= other.start and self.end >= other.end;
    }

    fn overlaps(self: Section, other: Section) bool {
        return self.contains(other.start) or self.contains(other.end);
    }

    fn fromIds(ids: []const u8) !Section {
        var parts = std.mem.split(u8, ids, "-");
        return Section{
            .start = try std.fmt.parseUnsigned(u8, parts.next().?, 10),
            .end = try std.fmt.parseUnsigned(u8, parts.next().?, 10),
        };
    }
};

const Pair = struct {
    first_elf: Section,
    second_elf: Section,

    fn fullyContains(self: Pair) bool {
        return self.first_elf.covers(self.second_elf) or self.second_elf.covers(self.first_elf);
    }

    fn partiallyContains(self: Pair) bool {
        return self.first_elf.overlaps(self.second_elf) or self.second_elf.overlaps(self.first_elf);
    }

    fn fromAssignment(assignment: []const u8) !Pair {
        var parts = std.mem.split(u8, assignment, ",");
        return Pair{
            .first_elf = try Section.fromIds(parts.next().?),
            .second_elf = try Section.fromIds(parts.next().?),
        };
    }
};

fn part1(assignments: []const u8) !usize {
    var parts = std.mem.split(u8, assignments, "\n");
    var count: usize = 0;
    while (parts.next()) |s| {
        if (std.mem.eql(u8, s, "")) {
            break;
        }
        const pair = try Pair.fromAssignment(s);
        if (pair.fullyContains()) {
            count += 1;
        }
    }
    return count;
}

fn part2(assignments: []const u8) !usize {
    var parts = std.mem.split(u8, assignments, "\n");
    var count: usize = 0;
    while (parts.next()) |s| {
        if (std.mem.eql(u8, s, "")) {
            break;
        }
        const pair = try Pair.fromAssignment(s);
        if (pair.partiallyContains()) {
            count += 1;
        }
    }
    return count;
}

test "part1" {
    const sample = @embedFile("./sample.txt");
    try std.testing.expectEqual(part1(sample), 2);
    const input = @embedFile("./input.txt");
    try std.testing.expectEqual(part1(input), 475);
}

test "part2" {
    const sample = @embedFile("./sample.txt");
    try std.testing.expectEqual(part2(sample), 4);
    const input = @embedFile("./input.txt");
    try std.testing.expectEqual(part2(input), 825);
}
