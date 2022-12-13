def part_1
  cd_regex = /\$ cd (.*)/
  ls_regex = /\$ ls/
  dir_regex = /dir (.*)/
  file_regex = /(\d+) (.*)/
  pwd = []
  sz = Hash.new(0)
  File.readlines("./input.txt").each do |line|
    case line
    when cd_regex
      $1 == ".." ? pwd.pop : pwd.push($1)
    when file_regex
      path = pwd.dup
      loop do
        sz[path.dup] += $1.to_i
        path.pop
        break if path.empty?
      end
    end
  end
  [sz, sz.select { |_, v| v <= 100_000 }.sum { |_, v| v }]
end

TOTAL = 70_000_000
NEEDED = 30_000_000
USABLE = TOTAL - NEEDED

sizes, = part_1
USED = sizes[["/"]]
TARGET = USED - USABLE

p sizes.select { |_, v| v >= TARGET }.values.min
