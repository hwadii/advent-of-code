#           rock 	paper	scissor
# rock		0        0        1
# paper		1        0        0
# scissor	0        1        0

class Tool
  ROCK = 0
  PAPER = 1
  SCISSOR = 2

  MATCHUPS = [
    nil, false, true,
    true, nil, false,
    false, true, nil,
  ]

  attr_reader :tool

  def initialize(tool)
    @tool = tool
  end

  def wins?(other)
    MATCHUPS[@tool * 3 + other.tool]
  end

  def score(other)
    if wins?(other).nil?
      @tool + 1 + 3
    elsif wins?(other)
      @tool + 1 + 6
    else
      @tool + 1 + 0
    end
  end
end

OPPONENT = {
  "A" => Tool.new(Tool::ROCK),
  "B" => Tool.new(Tool::PAPER),
  "C" => Tool.new(Tool::SCISSOR),
}

USER = {
  "X" => Tool.new(Tool::ROCK),
  "Y" => Tool.new(Tool::PAPER),
  "Z" => Tool.new(Tool::SCISSOR),
}

MAPPING = {
  "A" => { "X" => Tool.new(Tool::SCISSOR), "Y" => Tool.new(Tool::ROCK), "Z" => Tool.new(Tool::PAPER) },
  "B" => { "X" => Tool.new(Tool::ROCK), "Y" => Tool.new(Tool::PAPER), "Z" => Tool.new(Tool::SCISSOR) },
  "C" => { "X" => Tool.new(Tool::PAPER), "Y" => Tool.new(Tool::SCISSOR), "Z" => Tool.new(Tool::ROCK) },
}

def part_1
  total = File.readlines("./input.txt").map do |line|
    opponent, user = line.chomp.split(" ")
    USER[user].score(OPPONENT[opponent])
  end.inject(:+)
end

def part_2
  total = File.readlines("./input.txt").map do |line|
    opponent, user = line.chomp.split(" ")
    MAPPING[opponent][user].score(OPPONENT[opponent])
  end.inject(:+)
end

p part_2
