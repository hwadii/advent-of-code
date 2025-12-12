use std::collections::{HashMap, HashSet, VecDeque};

const SAMPLE: &str = include_str!("sample.txt");
const SAMPLE2: &str = include_str!("sample2.txt");
const INPUT: &str = include_str!("input.txt");

fn main() {
    dbg!(part1());
    // dbg!(part2());
}

fn part1() -> u64 {
    let graph = INPUT
        .lines()
        .map(|line| {
            let sides = line.split(": ").collect::<Vec<_>>();
            (sides[0], sides[1].split(" ").collect::<Vec<_>>())
        })
        .collect::<HashMap<&str, Vec<&str>>>();
    // write_graph(&graph);
    let ab = paths_from(&graph, "svr", "fft");
    let ac = paths_from(&graph, "svr", "dac");
    let bc = paths_from(&graph, "fft", "dac");
    let cb = paths_from(&graph, "dac", "fft");
    let cd = paths_from(&graph, "dac", "out");
    let bd = paths_from(&graph, "fft", "out");
    dbg!(ab, bc, cd);
    ab * bc * cd
}

fn paths_from(g: &HashMap<&str, Vec<&str>>, start: &str, end: &str) -> u64 {
    let mut memo = HashMap::<(&str, &str), u64>::new();
    let mut s = VecDeque::<&str>::from([start]);
    let mut cur = start;
    let mut paths = 0;
    while !s.is_empty() {
        if let Some(c) = s.pop_front() {
            // dbg!(c);
            if c == end {
                // dbg!(start, cur, end);
                paths += memo.get(&(start, cur)).unwrap();
                dbg!(paths);
                // dbg!(&memo);
                // dbg!(paths);
                // dbg!(&memo);
            }
            cur = c;
            if let Some(edges) = g.get(cur) {
                for edge in edges {
                    if let Some(upto) = memo.get(&(start, cur)) {
                        if let Some(count) = memo.get(&(start, edge)) {
                            memo.insert((start, edge), count + upto);
                        } else {
                            memo.insert((start, edge), *upto);
                            s.push_back(edge);
                        }
                    } else {
                        memo.insert((start, cur), 1);
                        s.push_back(edge);
                    }
                }
            }
        }
    }
    println!();
    println!();
    paths
}

type S<'a> = (&'a str, HashSet<&'a str>, bool, bool);

fn paths_through(g: &HashMap<&str, Vec<&str>>, start: &str, end: &str, a: &str, b: &str) -> u64 {
    let visited = HashSet::<&str>::new();
    let mut s = VecDeque::<S>::new();
    s.push_back((start, visited, false, false));
    let mut count = 0;
    while !s.is_empty() {
        if let Some((cur, visited, found_a, found_b)) = s.pop_front() {
            if cur == end {
                if found_a && found_b {
                    count += 1;
                }
                continue;
            }
            if let Some(edges) = g.get(cur) {
                for edge in edges {
                    let mut new_visited = visited.iter().copied().collect::<HashSet<&str>>();
                    new_visited.insert(edge);
                    s.push_back((
                        edge,
                        new_visited,
                        found_a || *edge == a,
                        found_b || *edge == b,
                    ));
                }
            }
        }
    }
    count
}

fn paths_from_rec(g: &HashMap<&str, Vec<&str>>, start: &str, end: &str, a: &str, b: &str) -> u64 {
    let mut visited = HashSet::<&str>::new();
    dfs(g, start, end, &mut visited, a, b, false, false)
}

fn dfs<'a>(
    g: &'a HashMap<&str, Vec<&str>>,
    start: &'a str,
    end: &str,
    visited: &mut HashSet<&'a str>,
    a: &str,
    b: &str,
    found_a: bool,
    found_b: bool,
) -> u64 {
    if start == end {
        return if found_a && found_b { 1 } else { 0 };
    }
    visited.insert(start);
    let mut count = 0;
    if let Some(edges) = g.get(start) {
        for edge in edges {
            count += dfs(
                g,
                edge,
                end,
                visited,
                a,
                b,
                found_a || *edge == a,
                found_b || *edge == b,
            );
        }
    }
    visited.remove(start);
    count
}

fn write_graph(g: &HashMap<&str, Vec<&str>>) {
    println!("digraph {{");
    g.iter().for_each(|(node, edges)| {
        println!("  {} -> {{{}}}", node, edges.join(" "));
    });
    println!("}}");
}
