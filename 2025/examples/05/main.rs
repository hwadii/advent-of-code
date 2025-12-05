use std::{collections::HashMap, ops::RangeInclusive};

const SAMPLE: &str = include_str!("sample.txt");
const INPUT: &str = include_str!("input.txt");

fn main() {
    dbg!(part1());
    dbg!(part2());
}

fn part1() -> u64 {
    let parts: Vec<&str> = INPUT.split("\n\n").collect();
    let fresh_ranges = parts[0]
        .lines()
        .map(|r| {
            r.split("-")
                .map(|n| n.parse::<u64>().unwrap())
                .collect::<Vec<u64>>()
        })
        .map(|r| r[0]..=r[1])
        .collect::<Vec<RangeInclusive<u64>>>();
    let ingredients = parts[1].lines().map(|id| id.parse::<u64>().unwrap());
    let fresh_ingredients =
        ingredients.filter(|id| fresh_ranges.iter().find(|r| r.contains(id)).is_some());
    fresh_ingredients.count() as u64
}

fn part2() -> u64 {
    let parts: Vec<&str> = INPUT.split("\n\n").collect();
    let fresh_ranges = parts[0]
        .lines()
        .map(|r| {
            r.split("-")
                .map(|n| n.parse::<u64>().unwrap())
                .collect::<Vec<u64>>()
        })
        .map(|r| r[0]..=r[1])
        .collect::<Vec<RangeInclusive<u64>>>();
    let mut ranges: Vec<RangeInclusive<u64>> = vec![];
    let (mut start, mut end) = fresh_ranges[0].clone().into_inner();
    for range in fresh_ranges {
        let current = start..=end;
        if current.end() >= range.start() && range.end() >= current.start() {
            end = range.end().clone().max(end);
        } else {
            ranges.push(current);
            start = range.start().clone();
            end = range.end().clone();
        }
    }
    ranges.push(start..=end);
    ranges.into_iter().map(|range| range.count() as u64).sum()
}
