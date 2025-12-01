const SAMPLE: &str = include_str!("sample.txt");
const INPUT: &str = include_str!("input.txt");

fn main() {
    dbg!(part1());
    dbg!(part2());
}

fn part1() -> usize {
    let mut code: i32 = 50;
    let mut count = 0;
    INPUT
        .lines()
        .for_each(|l| {
            let (direction, amount) = l.split_at(1);
            let amount = amount.parse::<i32>().unwrap();
            if direction == "L" {
                code -= amount;
            } else {
                code += amount;
            }
            count += if code.rem_euclid(100) == 0 { 1 } else { 0 };
        });
    count
}

fn part2() -> usize {
    let mut code: i32 = 50;
    let mut zero: usize = 0;
    INPUT
        .lines()
        .for_each(|l| {
            let (direction, amount) = l.split_at(1);
            let amount = amount.parse::<i32>().unwrap();
            let new_code = if direction == "L" {
                code - amount
            } else {
                code + amount
            };
            zero += new_code.abs().div_euclid(100) as usize;
            if new_code <= 0 && code > 0 {
                zero += 1
            }
            code = new_code.rem_euclid(100);
        });
    zero
}
