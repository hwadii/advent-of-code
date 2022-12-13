def part_1
  input = File.readlines("./input.txt").map { |line| line.chomp.chars.map(&:to_i) }
  length = input.count
  edges = 2 * (2 * length - 2)
  count = 0
  smaller_square = 1..(length - 2)
  smaller_square.each do |i| # x
    smaller_square.each do |j| # y
      # 2, 2: first pass y = 2, x = 0 => length | second pass x = 2, y = 0 => length

      is_visible = true
      0.upto(length - 1) do |k|
        if k == i
          break if is_visible
          is_visible = true
          next
        end
        is_shorter = input[k][j] < input[i][j]
        is_visible &&= is_shorter
      end

      if is_visible
        count += 1
        next
      end

      is_visible = true
      0.upto(length - 1) do |k|
        if k == j
          break if is_visible
          is_visible = true
          next
        end
        is_shorter = input[i][k] < input[i][j]
        is_visible &&= is_shorter
      end

      if is_visible
        count += 1
        next
      end
    end
  end

  count + edges
end

def part_2
  raw_input = File.read("./sample.txt")
  input = File.readlines("./input.txt").map { |line| line.chomp.chars.map(&:to_i) }
  len = input.length - 1
  max = -Float::INFINITY
  puts raw_input
  0.upto(len) do |i|
    0.upto(len) do |j|
      current = input[i][j]
      scenic_scores = []
      left = 0...j
      right = (j + 1)..len
      up = 0...i
      down = (i + 1)..len
      count = 0
      up.reverse_each do |y|
        considered = input[y][j]
        count += 1
        break if considered >= current
      end
      scenic_scores << count
      count = 0
      left.reverse_each do |x|
        considered = input[i][x]
        count += 1
        break if considered >= current
      end
      scenic_scores << count
      count = 0
      right.each do |x|
        considered = input[i][x]
        count += 1
        break if considered >= current
      end
      scenic_scores << count
      count = 0
      down.each do |y|
        considered = input[y][j]
        count += 1
        break if considered >= current
      end
      scenic_scores << count

      max = [max, scenic_scores.reduce(:*)].max
    end
  end
  max
end

p part_2
