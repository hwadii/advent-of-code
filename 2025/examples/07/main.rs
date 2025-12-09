
const SAMPLE: &str = include_str!("sample.txt");
const INPUT: &str = include_str!("input.txt");

fn main() {
    // dbg!(part1());
    dbg!(part2());
}

fn part1() -> u64 {
    let mut grid: Grid = Grid(
        SAMPLE
            .lines()
            .map(|line| {
                line.chars()
                    .map(|c| match c {
                        '.' => State::Empty,
                        'S' | '|' => State::Beam,
                        '^' => State::Splitter,
                        _ => unreachable!(),
                    })
                    .collect()
            })
            .collect(),
    );
    let mut splits = 0;
    for j in 0..grid.0.len() {
        let beams = grid.0[j]
            .iter()
            .enumerate()
            .filter_map(|(idx, s)| {
                if *s == State::Beam {
                    Some((idx, j))
                } else {
                    None
                }
            })
            .collect::<Vec<_>>();
        for beam in beams {
            let next = (beam.0, beam.1 + 1);
            if next.0 < grid.0[0].len() && next.1 < grid.0.len() {
                if grid.0[next.1][next.0] == State::Empty {
                    grid.0[next.1][next.0] = State::Beam;
                } else if grid.0[next.1][next.0] == State::Splitter {
                    splits += 1;
                    grid.0[next.1][next.0 - 1] = State::Beam;
                    grid.0[next.1][next.0 + 1] = State::Beam;
                }
            }
        }
    }
    // dbg!(start);
    // dbg!(&grid);
    splits
}

fn part2() -> u64 {
    let mut grid: Grid = Grid(
        SAMPLE
            .lines()
            .map(|line| {
                line.chars()
                    .map(|c| match c {
                        '.' => State::Empty,
                        'S' | '|' => State::Beam,
                        '^' => State::Splitter,
                        _ => unreachable!(),
                    })
                    .collect()
            })
            .collect(),
    );
    let start = grid.0[0]
        .iter()
        .enumerate()
        .find_map(|(idx, s)| {
            if *s == State::Beam {
                Some((idx, 0))
            } else {
                None
            }
        })
        .unwrap();
    let mut splits = 0;
    for j in 0..grid.0.len() {
        let beams = grid.0[j]
            .iter()
            .enumerate()
            .filter_map(|(idx, s)| {
                if *s == State::Beam {
                    Some((idx, j))
                } else {
                    None
                }
            })
            .collect::<Vec<_>>();
        for beam in beams {
            let next = (beam.0, beam.1 + 1);
            if next.0 < grid.0[0].len() && next.1 < grid.0.len() {
                if grid.0[next.1][next.0] == State::Empty {
                    grid.0[next.1][next.0] = State::Beam;
                } else if grid.0[next.1][next.0] == State::Splitter {
                    splits += 2;
                    grid.0[next.1][next.0 - 1] = State::Beam;
                    grid.0[next.1][next.0 + 1] = State::Beam;
                }
            }
        }
    }
    dbg!(&grid);
    splits - 2
    // println!("{:?}", &grid);
    // grid.0
    //     .last()
    //     .unwrap()
    //     .iter()
    //     .enumerate()
    //     .filter_map(|(idx, s)| {
    //         if *s == State::Beam {
    //             Some((idx, grid.0.len() - 1))
    //         } else {
    //             None
    //         }
    //     })
    //     .map(|end| {
    //         let mut s = vec![(start.clone(), vec![start.clone()])];
    //         let mut paths = 0;
    //         while !s.is_empty() {
    //             // dbg!(&visited);
    //             let (cur, path) = s.pop().unwrap();
    //             if cur == end {
    //                 paths += 1;
    //                 continue;
    //             }
    //             let so = (cur.0, cur.1.saturating_add(1));
    //             if grid.0.get(so.1).is_some()
    //                 && grid.0[so.1].get(so.0).is_some()
    //                 && grid.0[so.1][so.0] == State::Splitter
    //             {
    //                 let sw = (cur.0.saturating_sub(1), cur.1.saturating_add(1));
    //                 if grid.0.get(sw.1).is_some()
    //                     && grid.0[sw.1].get(sw.0).is_some()
    //                     && grid.0[sw.1][sw.0] == State::Beam
    //                 {
    //                     if !path.contains(&sw) {
    //                         let mut p = path.clone();
    //                         p.push(sw);
    //                         s.push((sw, p));
    //                     }
    //                 }
    //                 let se = (cur.0.saturating_add(1), cur.1.saturating_add(1));
    //                 if grid.0.get(se.1).is_some()
    //                     && grid.0[se.1].get(se.0).is_some()
    //                     && grid.0[se.1][se.0] == State::Beam
    //                 {
    //                     if !path.contains(&se) {
    //                         let mut p = path.clone();
    //                         p.push(se);
    //                         s.push((se, p));
    //                     }
    //                 }
    //             } else if grid.0.get(so.1).is_some()
    //                 && grid.0[so.1].get(so.0).is_some()
    //                 && grid.0[so.1][so.0] == State::Beam
    //             {
    //                 if !path.contains(&so) {
    //                     let mut p = path.clone();
    //                     p.push(so);
    //                     s.push((so, p));
    //                 }
    //             }
    //         }
    //         // dbg!(&paths);
    //         // dbg!(paths.iter().filter(|beams| beams.contains(&start)).unique().count() as u64);
    //         dbg!(&end);
    //         paths
    //     })
    //     .sum()
    // dbg!(start, ends);
    // dbg!(start);
    // dbg!(&grid);
}

#[derive(Debug, PartialEq, Eq)]
enum State {
    Beam,
    Empty,
    Splitter,
}

struct Grid(Vec<Vec<State>>);

impl std::fmt::Debug for Grid {
    fn fmt(&self, f: &mut std::fmt::Formatter<'_>) -> std::fmt::Result {
        for j in 0..self.0.len() {
            for i in 0..self.0[j].len() {
                match &self.0[j][i] {
                    State::Empty => f.write_str("."),
                    State::Beam => f.write_str("|"),
                    State::Splitter => f.write_str("^"),
                }?
            }
            f.write_str("\n")?;
        }
        Ok(())
    }
}
