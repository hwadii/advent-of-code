def solve(n)
  input = File.readlines("./input.txt").first.chomp
  input.chars.each_cons(n).with_index do |four, index|
    is_marker = four.uniq.count == four.count
    return index + four.count if is_marker
  end
end

part1 = solve(4)
part2 = solve(14)

p part2
