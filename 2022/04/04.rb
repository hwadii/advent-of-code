def part_1(path)
  File.readlines(path).select do |line|
    line = line.chomp.split(",")
    first_elf, second_elf = line.map { |pair| pair.split("-").map(&:to_i) }
    first_elf_range, second_elf_range = (first_elf[0]..first_elf[1]), (second_elf[0]..second_elf[1])
    first_elf_range.cover?(second_elf_range) || second_elf_range.cover?(first_elf_range)
  end.size
end

def part_2(path)
  File.readlines(path).select do |line|
    line = line.chomp.split(",")
    first_elf, second_elf = line.map { |pair| pair.split("-").map(&:to_i) }
    first_elf_range, second_elf_range = (first_elf[0]..first_elf[1]), (second_elf[0]..second_elf[1])
    second_elf_range.include?(first_elf_range.begin) || second_elf_range.include?(first_elf_range.end) || first_elf_range.include?(second_elf_range.begin) || first_elf_range.include?(second_elf_range.end)
  end.size
end
