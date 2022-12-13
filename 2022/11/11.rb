class Monkey
  attr_accessor :items, :operation, :test

  Test = Struct.new(:value, :if_true, :if_false)

  Op = Struct.new(:op, :rhs) do
    def apply(worry, other)
      case op
      when "+" then worry + other
      when "-" then worry - other
      when "*" then worry * other
      when "/" then worry / other
      end
    end
  end

  def initialize(attrs = {})
    @operation = Op.new(attrs[:op], attrs[:rhs])
    @test = Test.new(attrs[:value], attrs[:if_true], attrs[:if_false])
  end

  def divisible?(worry) = worry % @test.value == 0

  def apply_operation(worry)
    case @operation.rhs
    when /\d+/
      @operation.apply(worry, $&.to_i)
    when /old/
      @operation.apply(worry, worry)
    end
  end
end

MONKEY_NUMBER_REGEXP = /Monkey \d:/
STARTING_ITEMS_REGEXP = /Starting items: ((\d+(,\s)*)*)/
OPERATION_REGEXP = /Operation: new = old ([\+\-\*\/]) (old|\d+)/
TEST_REGEXP = /Test: divisible by (\d+)/
TRUE_REGEXP = /If true: throw to monkey (\d+)/
FALSE_REGEXP = /If false: throw to monkey (\d+)/

def parse_monkeys
  File.read("./input.txt").split("\n\n").map do |monkey_attrs|
    monkey = Monkey.new
    monkey_attrs.split("\n").map do |attrs|
      attrs.strip!
      case attrs
      when STARTING_ITEMS_REGEXP
        monkey.items = $1.split(", ").map(&:to_i)
      when OPERATION_REGEXP
        monkey.operation.op = $1
        monkey.operation.rhs = $2
      when TEST_REGEXP
        monkey.test.value = $1.to_i
      when TRUE_REGEXP
        monkey.test.if_true = $1.to_i
      when FALSE_REGEXP
        monkey.test.if_false = $1.to_i
      end
    end
    monkey
  end
end

def monkey_business(monkeys, count, divide_by_3: false)
  inspections = Array.new(monkeys.count) { 0 }
  lcm = monkeys.map { |m| m.test.value }.inject(:lcm)
  count.times do |i|
    monkeys.each.with_index do |monkey, i|
      loop do
        break if monkey.items.empty?
        inspections[i] += 1
        item = monkey.apply_operation(monkey.items.shift)
        item = divide_by_3 ? item / 3 : item % lcm
        if monkey.divisible?(item)
          monkeys[monkey.test.if_true].items.push(item)
        else
          monkeys[monkey.test.if_false].items.push(item)
        end
      end
    end
  end
  inspections
end

p monkey_business(parse_monkeys, 20, divide_by_3: true).sort.last(2).inject(:*)
p monkey_business(parse_monkeys, 20, divide_by_3: false).sort.last(2).inject(:*)
p monkey_business(parse_monkeys, 10_000, divide_by_3: false).sort.last(2).inject(:*)
