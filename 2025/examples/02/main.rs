use itertools::Itertools;
use std::collections::HashMap;

const SAMPLE: &str = include_str!("sample.txt");
const INPUT: &str = include_str!("input.txt");

fn main() {
    dbg!(part1());
    dbg!(part2());
}

fn part1() -> u64 {
    INPUT
        .strip_suffix("\n")
        .unwrap()
        .split(",")
        .flat_map(|range| {
            let range = range.split("-").collect::<Vec<&str>>();
            let (start, end) = (
                range[0].parse::<u64>().unwrap(),
                range[1].parse::<u64>().unwrap(),
            );
            (start..=end).map(|n| {
                let s = n.to_string();
                let (left, right) = s.split_at(s.len() / 2);
                if left == right {
                    n
                } else {
                    0
                }
            })
        })
        .sum()
}

fn part2() -> u64 {
    let products: HashMap<u64, Vec<(u64, u64)>> = HashMap::from([
        (10, vec![(10, 1), (5, 2), (2, 5)]),
        (9, vec![(9, 1), (3, 3)]),
        (8, vec![(8, 1), (4, 2), (2, 4)]),
        (7, vec![(7, 1)]),
        (6, vec![(6, 1), (3, 2), (2, 3)]),
        (5, vec![(5, 1)]),
        (4, vec![(4, 1), (2, 2)]),
        (3, vec![(3, 1)]),
        (2, vec![(2, 1)]),
        (1, vec![(0, 1)]),
    ]);
    INPUT
        .strip_suffix("\n")
        .unwrap()
        .split(",")
        .flat_map(|range| {
            let range = range.split("-").collect::<Vec<&str>>();
            let (start, end) = (
                range[0].parse::<u64>().unwrap(),
                range[1].parse::<u64>().unwrap(),
            );
            (start..=end).flat_map(|n| -> _ {
                let s = n.to_string();
                products
                    .get(&(s.len() as u64))
                    .unwrap()
                    .into_iter()
                    .map(move |pair| {
                        let cand = &s[..(pair.1 as usize)];
                        if cand.repeat(pair.0 as usize) == s {
                            n
                        } else {
                            0
                        }
                    })
                    .dedup()
            })
        })
        .sum()
}
