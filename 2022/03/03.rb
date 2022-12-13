require 'set'

PRIORITIES = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
  .split('')
  .map
  .with_index { |letter, index| [letter, index + 1] }
  .to_h

def part_1
  out = File.readlines("./input.txt").map do |line|
    line = line.chomp
    part, other = line[..(line.length / 2) - 1].split(''), line[(line.length / 2)..].split('')
    Set.new(part).intersection(Set.new(other)).to_a.first
  end.map { |l| PRIORITIES[l] }.sum
end

def part_2
  letters = []
  out = File.readlines("./input.txt").each_slice(3) do |lines|
    first, second, third = lines.map { |line| Set.new(line.chomp.chars) }
    letters << PRIORITIES[first.intersection(second).intersection(third).to_a.first]
  end
  letters.sum
end

p part_2
