use std::collections::{BTreeSet, HashMap, HashSet};

const SAMPLE: &str = include_str!("sample.txt");
const INPUT: &str = include_str!("input.txt");

fn main() {
    dbg!(part1());
    // dbg!(part2());
}

fn part1() -> u64 {
    let mut junction_boxes = SAMPLE
        .lines()
        .map(|line| {
            let coords = line
                .split(",")
                .filter_map(|n| n.parse::<usize>().ok())
                .collect::<Vec<_>>();
            JunctionBox {
                x: coords[0],
                y: coords[1],
                z: coords[2],
            }
        })
        .collect::<Vec<_>>();
    let mut circuits = Vec::<HashSet<JunctionBox>>::new();
    for jb in &junction_boxes {
        let closest_box = junction_boxes
            .iter()
            .filter(|other| jb != *other)
            .min_by_key(|a| a.distance(jb))
            .unwrap();
        circuits.push(HashSet::from([jb.clone(), closest_box.clone()]));
        // dbg!(closest_box);
    }
    // circuits.sort_by_key(|circuit| circuit..distance(circuit[1]));
    let mut connections = 0;
    while connections < 10 {
        for (i, circuit) in circuits.iter_mut().enumerate() {
        }
        connections += 1;
    }
    dbg!(circuits);
    // let mut connections = 0;
    // let mut circuits = Vec::<HashSet<JunctionBox>>::new();
    0
}

#[derive(Debug, PartialEq, Eq, Hash, Clone)]
struct JunctionBox {
    x: usize,
    y: usize,
    z: usize,
}

impl JunctionBox {
    fn distance(&self, other: &JunctionBox) -> u64 {
        f64::sqrt(
            (self.x as i64 - other.x as i64).pow(2) as f64
                + (self.y as i64 - other.y as i64).pow(2) as f64
                + (self.z as i64 - other.z as i64).pow(2) as f64,
        ) as u64
    }
}
