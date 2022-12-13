def part_1
  crate_stacks, moves = File.read("./input.txt").split("\n\n")
  crate_stacks = crate_stacks.split("\n")
  length = crate_stacks.first.length
  stacks = Array.new(length) { [] }
  crate_stacks.each do |letters|
    letters.chars.each.with_index do |c, i|
      next if c == " "
      stacks[i] << c
    end
  end
  moves = moves.split("\n").map { |move| move.split(" ").map(&:to_i) }
  moves.each do |m|
    amt, from, to = m[0], m[1] - 1, m[2] - 1
    to_move = stacks[from].shift(amt)
    stacks[to].unshift(*to_move.reverse)
  end
  stacks.map(&:first).join
end

def part_2
  crate_stacks, moves = File.read("./sample.txt").split("\n\n")
  crate_stacks = crate_stacks.split("\n")
  length = crate_stacks.first.length
  stacks = Array.new(length) { [] }
  crate_stacks.each do |letters|
    letters.chars.each.with_index do |c, i|
      next if c == " "
      stacks[i] << c
    end
  end
  moves = moves.split("\n").map { |move| move.split(" ").map(&:to_i) }
  moves.each do |m|
    amt, from, to = m[0], m[1] - 1, m[2] - 1
    to_move = stacks[from].shift(amt)
    stacks[to].unshift(*to_move)
  end
  stacks.map(&:first).join
end

p part_2
