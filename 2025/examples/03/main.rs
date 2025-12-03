const SAMPLE: &str = include_str!("sample.txt");
const INPUT: &str = include_str!("input.txt");

fn main() {
    dbg!(part1());
    dbg!(part2());
}

fn part1() -> u64 {
    joltage(INPUT, 2)
}

fn part2() -> u64 {
    joltage(INPUT, 12)
}

fn joltage(input: &str, target: usize) -> u64 {
    input
        .lines()
        .map(|line| {
            let mut n = String::new();
            let mut left = 0;
            while n.len() < target {
                let right = line.len() - target + n.len() + 1;
                let mut max_i = 0;
                let mut max = 0;
                line[left..right]
                    .chars()
                    .flat_map(|c| c.to_digit(10))
                    .enumerate()
                    .for_each(|(i, digit)| {
                        if digit > max {
                            max = digit;
                            max_i = i;
                        }
                    });
                left += max_i + 1;
                n.push_str(&max.to_string());
            }
            n.parse::<u64>().unwrap()
        })
        .sum()
}
