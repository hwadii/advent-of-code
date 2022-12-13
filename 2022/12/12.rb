require 'rb_heap/heap'

INPUT = File.readlines("./input.txt").map do |line|
  line = line.chomp
  line.chars
end

WIDTH = INPUT[0].length
HEIGHT = INPUT.length
DIRECTIONS = [Vector[0, -1], Vector[0, 1], Vector[1, 0], Vector[-1, 0]]

def targets(input)
  start_pos = nil
  end_pos = nil
  (0...HEIGHT).each do |i|
    (0...WIDTH).each do |j|
      start_pos = Vector[i, j] if INPUT[i][j] == "S"
      end_pos = Vector[i, j] if INPUT[i][j] == "E"
      return [start_pos, end_pos] if start_pos && end_pos
    end
  end
end

def starts(input)
  values = []
  (0...HEIGHT).each do |i|
    (0...WIDTH).each do |j|
      values << Vector[i, j] if input[i][j] == "a"
    end
  end
  values
end

def height(input, spot)
  letter = input[spot[0]][spot[1]]
  case letter
  when "S" then "a".ord
  when "E" then "z".ord
  else letter.ord
  end
end

def origins_for(input, spot)
  DIRECTIONS.filter_map do |direction|
    next if spot.nil?
    origin = spot + direction
    next unless input.dig(origin[0], origin[1])
    next unless height(input, spot) - height(input, origin) <= 1

    origin
  end
end

def dijkstra(input, target)
  q = Heap.new { |a, b| a[0] < b[0] }
  q << [0, target]
  distances = { target => 0 }

  until q.empty?
    cost, spot = q.pop

    DIRECTIONS.each do |move|
      neighbor = spot + move
      next unless input.dig(neighbor[0], neighbor[1])
      next unless height(input, spot) - height(input, neighbor) <= 1
      new_cost = cost + 1
      next if distances.key?(neighbor) && new_cost >= distances[neighbor]
      distances[neighbor] = new_cost
      q << [new_cost, neighbor]
    end
  end
  distances
end

source, target = targets(INPUT)

distances = dijkstra(INPUT, target)
p distances[source]
p distances.values_at(*starts(INPUT)).compact.min
