const std = @import("std");
const print = std.debug.print;

const Move = enum(u32) {
    rock,
    paper,
    scissors,

    const matchups = [_]Outcome{ Outcome.draw, Outcome.loss, Outcome.win, Outcome.win, Outcome.draw, Outcome.loss, Outcome.loss, Outcome.win, Outcome.draw };

    const all_moves = [3]Move{ Move.rock, Move.paper, Move.scissors };

    pub fn points(self: Move) u8 {
        return switch (self) {
            Move.rock => 1,
            Move.paper => 2,
            Move.scissors => 3,
        };
    }

    pub fn outcome(self: Move, theirs: Move) Outcome {
        return matchups[@enumToInt(self) * 3 + @enumToInt(theirs)];
    }

    fn matchingMove(self: Move, oc: Outcome) Move {
        const start = @enumToInt(self) * 3;
        const end = start + 3;
        const slice = Move.matchups[start..end];
        for (slice) |value, i| {
            if (value == oc) {
                return all_moves[i];
            }
        }
        unreachable;
    }

    pub fn winningMove(self: Move) Move {
        return self.matchingMove(Outcome.win);
    }

    pub fn losingMove(self: Move) Move {
        return self.matchingMove(Outcome.loss);
    }

    pub fn drawingMove(self: Move) Move {
        return self.matchingMove(Outcome.draw);
    }

    pub fn fromChar(c: []const u8) Move {
        if (std.mem.eql(u8, c, "A") or std.mem.eql(u8, c, "X")) {
            return Move.rock;
        } else if (std.mem.eql(u8, c, "B") or std.mem.eql(u8, c, "Y")) {
            return Move.paper;
        } else if (std.mem.eql(u8, c, "C") or std.mem.eql(u8, c, "Z")) {
            return Move.scissors;
        } else {
            unreachable;
        }
    }
};

const Round = struct {
    theirs: Move,
    ours: Move,

    pub fn outcome(self: Round) Outcome {
        return self.ours.outcome(self.theirs);
    }

    pub fn ourScore(self: Round) u32 {
        return self.ours.points() + self.outcome().points();
    }

    pub fn parseRound(round: []const u8) !Round {
        var smaller_parts = std.mem.split(u8, round, " ");
        var theirs = smaller_parts.next().?;
        var ours = smaller_parts.next().?;
        return Round{
            .theirs = Move.fromChar(theirs),
            .ours = Move.fromChar(ours),
        };
    }

    pub fn parseRoundOutcome(round: []const u8) !Round {
        var smaller_parts = std.mem.split(u8, round, " ");
        var their_move = Move.fromChar(smaller_parts.next().?);
        var round_outcome = Outcome.fromChar(smaller_parts.next().?);
        var our_move = round_outcome.matchingMove(their_move);
        return Round{ .theirs = their_move, .ours = our_move };
    }
};

const Outcome = enum {
    loss,
    draw,
    win,

    pub fn points(self: Outcome) u8 {
        return switch (self) {
            Outcome.win => 6,
            Outcome.draw => 3,
            Outcome.loss => 0,
        };
    }

    pub fn matchingMove(self: Outcome, theirs: Move) Move {
        return switch (self) {
            Outcome.loss => theirs.winningMove(),
            Outcome.draw => theirs.drawingMove(),
            Outcome.win => theirs.losingMove(),
        };
    }

    pub fn fromChar(c: []const u8) Outcome {
        if (std.mem.eql(u8, c, "X")) {
            return Outcome.loss;
        } else if (std.mem.eql(u8, c, "Y")) {
            return Outcome.draw;
        } else if (std.mem.eql(u8, c, "Z")) {
            return Outcome.win;
        } else {
            unreachable;
        }
    }
};

fn part1(input: []const u8) !u32 {
    var parts = std.mem.split(u8, input, "\n");
    var sum: u32 = 0;
    while (parts.next()) |part| {
        if (std.mem.eql(u8, part, "")) {
            break;
        }
        var round = try Round.parseRound(part);
        sum += round.ourScore();
    }
    return sum;
}

fn part2(input: []const u8) !u32 {
    var parts = std.mem.split(u8, input, "\n");
    var sum: u32 = 0;
    while (parts.next()) |part| {
        if (std.mem.eql(u8, part, "")) {
            break;
        }
        var round = try Round.parseRoundOutcome(part);
        sum += round.ourScore();
    }
    return sum;
}

test "part1" {
    const sample = @embedFile("./sample.txt");
    try std.testing.expectEqual(part1(sample), 15);

    const input = @embedFile("./input.txt");
    try std.testing.expectEqual(part1(input), 13565);
}

test "part2" {
    const sample = @embedFile("./sample.txt");
    try std.testing.expectEqual(part2(sample), 12);

    const input = @embedFile("./input.txt");
    try std.testing.expectEqual(part2(input), 12424);
}
