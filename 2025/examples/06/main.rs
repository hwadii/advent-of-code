use std::{collections::HashMap, ops::RangeInclusive};

const SAMPLE: &str = include_str!("sample.txt");
const INPUT: &str = include_str!("input.txt");

fn main() {
    // dbg!(part1());
    dbg!(part2());
}

fn part1() -> u64 {
    let parts = INPUT.split("\n\n").collect::<Vec<&str>>();
    let operands = parts[0]
        .lines()
        .map(|line| {
            line.split_ascii_whitespace()
                .map(|n| n.parse::<u64>().unwrap())
                .collect::<Vec<u64>>()
        })
        .collect::<Vec<Vec<u64>>>();
    let operators = parts[1].split_ascii_whitespace().collect::<Vec<&str>>();
    let mut running = operands.clone()[0].clone();
    for row in operands.iter().skip(1) {
        for (num_idx, num) in row.into_iter().enumerate() {
            let op = operators[num_idx];
            running[num_idx] = match op {
                "*" => running[num_idx] * num,
                "+" => running[num_idx] + num,
                _ => unreachable!(),
            };
        }
    }
    running.into_iter().sum()
}

fn part2() -> u64 {
    let parts = INPUT.split("\n\n").collect::<Vec<&str>>();
    let operands = parts[0].lines().collect::<Vec<&str>>();
    let operators = parts[1].split_ascii_whitespace().collect::<Vec<&str>>();
    let mut op_idx = 0;
    let mut idx = 0;
    let mut operator = operators[op_idx];
    let mut current = if operator == "*" { 1 } else { 0 };
    let mut total = 0;
    loop {
        let mut cs = Vec::<Option<char>>::new();
        for i in 0..operands.len() {
            cs.push(operands[i].chars().nth(idx));
        }
        if cs.iter().all(|c| c.is_none()) {
            total += current;
            break;
        }
        let s = cs.iter().map(|c| c.unwrap()).collect::<String>();
        if s.trim().is_empty() {
            idx += 1;
            op_idx += 1;
            operator = operators[op_idx];
            total += current;
            current = if operator == "*" { 1 } else { 0 };
            continue;
        } else {
            idx += 1;
            let n = s.trim().parse::<u64>().unwrap();
            current = match operator {
                "*" => current * n,
                "+" => current + n,
                _ => unreachable!(),
            };
        }
    }
    total
}
