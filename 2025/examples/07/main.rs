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
        INPUT
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
    let mut cols = vec![0; grid.0[0].len()];
    cols[start.0] = 1;
    let mut total = 1;
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
                    total += cols[beam.0];
                    cols[beam.0 - 1] += cols[beam.0];
                    cols[beam.0 + 1] += cols[beam.0];
                    cols[beam.0] = 0;
                    grid.0[next.1][next.0 - 1] = State::Beam;
                    grid.0[next.1][next.0 + 1] = State::Beam;
                }
            }
        }
    }
    total
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
