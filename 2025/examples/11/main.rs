use std::collections::{HashMap, HashSet, VecDeque};

const SAMPLE: &str = include_str!("sample.txt");
const SAMPLE2: &str = include_str!("sample2.txt");
const INPUT: &str = include_str!("input.txt");

fn main() {
    dbg!(part1());
    dbg!(part2());
}

fn part1() -> u64 {
    let graph = INPUT
        .lines()
        .map(|line| {
            let sides = line.split(": ").collect::<Vec<_>>();
            (sides[0], sides[1].split(" ").collect::<Vec<_>>())
        })
        .collect::<HashMap<&str, Vec<&str>>>();
    let mut memo = HashMap::<(&str, bool, bool), u64>::new();
    dfs(&mut memo, &graph, "you", "out", true, true)
}

fn part2() -> u64 {
    let graph = INPUT
        .lines()
        .map(|line| {
            let sides = line.split(": ").collect::<Vec<_>>();
            (sides[0], sides[1].split(" ").collect::<Vec<_>>())
        })
        .collect::<HashMap<&str, Vec<&str>>>();
    let mut memo = HashMap::<(&str, bool, bool), u64>::new();
    dfs(&mut memo, &graph, "svr", "out", false, false)
}

fn dfs<'a>(
    memo: &mut HashMap<(&'a str, bool, bool), u64>,
    g: &'a HashMap<&str, Vec<&str>>,
    start: &'a str,
    end: &str,
    fount_fft: bool,
    found_dac: bool,
) -> u64 {
    if start == end {
        if fount_fft && found_dac {
            1
        } else {
            0
        }
    } else {
        let mut count = 0;
        if let Some(edges) = g.get(start) {
            for edge in edges {
                let new_found_fft = fount_fft || *edge == "fft";
                let new_found_dac = found_dac || *edge == "dac";
                if let Some(c) = memo.get(&(edge, new_found_fft, new_found_dac)) {
                    count += c;
                } else {
                    let c = dfs(memo, g, edge, end, new_found_fft, new_found_dac);
                    count += c;
                    memo.insert((edge, new_found_fft, new_found_dac), c);
                }
            }
        }
        count
    }
}

fn write_graph(g: &HashMap<&str, Vec<&str>>) {
    println!("digraph {{");
    g.iter().for_each(|(node, edges)| {
        println!("  {} -> {{{}}}", node, edges.join(" "));
    });
    println!("}}");
}
