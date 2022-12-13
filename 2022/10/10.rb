PROGRAM = File.readlines("./input.txt").map do |line|
  instruction, value = line.chomp.split(" ")
  [instruction.to_sym, value&.to_i].compact
end

def execute_program(program)
  xs = []
  x = 1
  program.each do |instruction|
    xs.push(x)
    case instruction
        in [:addx, value]
        xs.push(x)
        x += value
        in [:noop]
    end
  end
  xs
end

def draw(xs)
  xs.each.with_index do |x, i|
    x = xs[i]
    pixel = i % 40
    char = ((x - 1)..(x + 1)).cover?(pixel) ? '#' : ' '
    printf char
    puts "\n" if (i + 1) % 40 == 0
  end
end

cycles = execute_program(PROGRAM)
draw(cycles)
