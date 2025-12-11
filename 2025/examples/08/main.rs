use disjoint_sets::UnionFind;
use itertools::Itertools;

const SAMPLE: &str = include_str!("sample.txt");
const INPUT: &str = include_str!("input.txt");

fn main() {
    dbg!(part1());
    dbg!(part2());
}

fn part1() -> u64 {
    let junction_boxes = INPUT
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
    let mut edges = junction_boxes
        .iter()
        .enumerate()
        .flat_map(|(i, _)| {
            junction_boxes
                .iter()
                .enumerate()
                .filter_map(move |(j, _)| if i != j { Some((i, j)) } else { None })
        })
        .collect::<Vec<_>>();
    edges.sort_by_key(|edge| junction_boxes[edge.0].distance(&junction_boxes[edge.1]));
    edges.dedup_by(|edge, other| edge.0 == other.1 && edge.1 == other.0);
    let mut uf = UnionFind::<usize>::new(junction_boxes.len());
    let mut connections = 0;
    for edge in &edges {
        uf.union(edge.0, edge.1);
        connections += 1;
        if connections == 1000 {
            break;
        }
    }
    let mut counts = uf
        .to_vec()
        .iter()
        .counts()
        .into_iter()
        .map(|pair| (*pair.0, pair.1))
        .collect::<Vec<_>>();
    counts.sort_by_key(|c| c.1);
    counts
        .iter()
        .rev()
        .take(3)
        .map(|pair| pair.1 as u64)
        .product::<u64>()
}

fn part2() -> u64 {
    let junction_boxes = INPUT
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
    let mut edges = junction_boxes
        .iter()
        .enumerate()
        .flat_map(|(i, _)| {
            junction_boxes
                .iter()
                .enumerate()
                .filter_map(move |(j, _)| if i != j { Some((i, j)) } else { None })
        })
        .collect::<Vec<_>>();
    edges.sort_by_key(|edge| junction_boxes[edge.0].distance(&junction_boxes[edge.1]));
    edges.dedup_by(|edge, other| edge.0 == other.1 && edge.1 == other.0);
    let mut uf = UnionFind::<usize>::new(junction_boxes.len());
    let mut connecting_edge = edges[0];
    for edge in &edges {
        uf.union(edge.0, edge.1);
        if (0..junction_boxes.len()).map(|i| uf.find(i)).all_equal() {
            connecting_edge = edge.clone();
            break;
        }
    }
    junction_boxes[connecting_edge.0].x as u64 * junction_boxes[connecting_edge.1].x as u64
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
