require "matrix"
require "set"

INSTRUCTIONS = File.readlines("./sample.txt").map do |line|
  line = line.chomp
  direction, value = line.split
  [direction.to_sym, value.to_i]
end

DIRECTIONS = {
  U: Vector[0, 1],
  D: Vector[0, -1],
  R: Vector[1, 0],
  L: Vector[-1, 0],
}

def rope(knots)
  [Vector[0, 0]] * knots
end

def motion(head, tail)
  dv = head - tail
  dv.any? { |d| d.abs > 1 } ? dv.map { |d| d <=> 0 } : Vector[0, 0] # checks adjacency
end

def path_for(rope, instructions)
  path = Set.new
  instructions.each do |direction, value|
    value.times do
      rope[0] += DIRECTIONS[direction]
      (1...rope.length).each { |i| rope[i] += motion(rope[i - 1], rope[i])}
      path.add(rope.last)
    end
  end
  path
end

p path_for(rope(2), INSTRUCTIONS)
p path_for(rope(10), INSTRUCTIONS).count
