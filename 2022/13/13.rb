INPUT = File.read("./input.txt").split("\n\n").map do |lines|
  lines.split("\n").map do |line|
    line = line.chomp
    eval(line)
  end
end

def compare_packets(left, right)
  (0...[left.size, right.size].max).each do |index|
    right_element = right[index]
    left_element = left[index]
    return -1 if left_element.nil?
    return 1 if right_element.nil?

    case [left_element.class, right_element.class]
    when [Integer, Integer]
      return -1 if left_element < right_element
      return 1 if left_element > right_element
    else
      left_element = Array(left_element)
      right_element = Array(right_element)
      result = compare_packets(left_element, right_element)
      return -1 if result < 0
      return 1 if result > 0
    end
  end
  0
end

packets_order = INPUT.map do |packets|
  left, right = packets
  compare_packets(left, right)
end

p packets_order.map.with_index { |p, i| p == -1 ? i + 1 : 0 }.sum
DIVIDERS = [[[2]], [[6]]].freeze

p (INPUT.flatten(1) + DIVIDERS)
    .sort { |a, b| compare_packets(a, b) }
    .filter_map
    .with_index { |packet, i| DIVIDERS.include?(packet) ? i + 1 : nil }
    .inject(:*)
