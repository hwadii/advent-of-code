use std::{collections::{BTreeSet, HashMap, HashSet}, process::Termination};
use itertools::Itertools;
use disjoint_sets::UnionFind;

const SAMPLE: &str = include_str!("sample.txt");
const INPUT: &str = include_str!("input.txt");

fn main() {
    dbg!(part1());
    // dbg!(part2());
}

fn part1() -> u64 {
    let mut junction_boxes = INPUT
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
    let mut uf = UnionFind::new(junction_boxes.len());
    let mut connections = 0;
    while connections < 1000 {
        let mut next_closest: Option<(usize, usize)> = None;
        let mut distance = usize::MAX;
        for (i, jb) in junction_boxes.iter().enumerate() {
            let closest = junction_boxes
                .iter()
                .enumerate()
                .filter(|(j, _)| !uf.equiv(i, *j))
                .min_by_key(|(_, other)| jb.distance(other));
            if let Some(c) = closest {
                let new_distance = jb.distance(c.1);
                if next_closest.is_some() {
                    if new_distance < distance {
                        next_closest = Some((i, c.0));
                        distance = new_distance;
                    }
                } else {
                    next_closest = Some((i, c.0));
                    distance = new_distance;
                }
            }
        }
        if let Some(nc) = next_closest {
            uf.union(nc.0, nc.1);
        }
        // dbg!(connections);
        dbg!(uf.to_vec().into_iter().counts().iter().k_largest(3).map(|c| c.1).product::<usize>());
        connections += 1;
    }
    // let mut sizes = (0..junction_boxes.len()).map(|i| uf.get(i).size()).collect::<Vec<_>>();
    // sizes.sort();
    // dbg!(&sizes);
    // dbg!(uf);
    // dbg!(indices.iter().map(|i| &junction_boxes[*i]).collect::<Vec<_>>());
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
    fn distance(&self, other: &JunctionBox) -> usize {
        ((self.x as i64 - other.x as i64).pow(2)
            + (self.y as i64 - other.y as i64).pow(2)
            + (self.z as i64 - other.z as i64).pow(2)) as usize
    }
}
