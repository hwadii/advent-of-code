use std::collections::HashSet;

const SAMPLE: &str = include_str!("sample.txt");
const INPUT: &str = include_str!("input.txt");

const DIRECTIONS: [Direction; 8] = [
    Direction::N,
    Direction::NE,
    Direction::E,
    Direction::SE,
    Direction::S,
    Direction::SW,
    Direction::W,
    Direction::NW,
];

fn main() {
    // dbg!(part1());
    dbg!(part2());
}

fn part1() -> u64 {
    let rolls_of_paper: HashSet<Roll> = INPUT
        .lines()
        .enumerate()
        .flat_map(|(y, line)| {
            line.chars()
                .enumerate()
                .filter(|(_, c)| *c == '@')
                .map(move |(x, _)| Roll {
                    x: x as i32,
                    y: y as i32,
                })
        })
        .collect();
    rolls_of_paper
        .clone()
        .into_iter()
        .map(|roll| {
            let mut n = 0;
            DIRECTIONS.into_iter().for_each(|direction| {
                let next = match direction {
                    Direction::N => Roll {
                        x: roll.x,
                        y: roll.y - 1,
                    },
                    Direction::NE => Roll {
                        x: roll.x + 1,
                        y: roll.y - 1,
                    },
                    Direction::E => Roll {
                        x: roll.x + 1,
                        y: roll.y,
                    },
                    Direction::SE => Roll {
                        x: roll.x + 1,
                        y: roll.y + 1,
                    },
                    Direction::S => Roll {
                        x: roll.x,
                        y: roll.y + 1,
                    },
                    Direction::SW => Roll {
                        x: roll.x - 1,
                        y: roll.y + 1,
                    },
                    Direction::W => Roll {
                        x: roll.x - 1,
                        y: roll.y,
                    },
                    Direction::NW => Roll {
                        x: roll.x - 1,
                        y: roll.y - 1,
                    },
                };
                n += if rolls_of_paper.contains(&next) { 1 } else { 0 };
            });
            if n < 4 {
                1
            } else {
                0
            }
        })
        .sum()
}

fn part2() -> u64 {
    let mut rolls_of_paper: HashSet<Roll> = INPUT
        .lines()
        .enumerate()
        .flat_map(|(y, line)| {
            line.chars()
                .enumerate()
                .filter(|(_, c)| *c == '@')
                .map(move |(x, _)| Roll {
                    x: x as i32,
                    y: y as i32,
                })
        })
        .collect();
    let mut total = 0;
    let mut to_remove = HashSet::<Roll>::new();
    loop {
        for roll in &to_remove {
            rolls_of_paper.remove(&roll);
        }
        total += to_remove.len() as u64;
        to_remove.clear();
        rolls_of_paper.clone().into_iter().for_each(|roll| {
            let mut n = 0;
            DIRECTIONS.into_iter().for_each(|direction| {
                let next = match direction {
                    Direction::N => Roll {
                        x: roll.x,
                        y: roll.y - 1,
                    },
                    Direction::NE => Roll {
                        x: roll.x + 1,
                        y: roll.y - 1,
                    },
                    Direction::E => Roll {
                        x: roll.x + 1,
                        y: roll.y,
                    },
                    Direction::SE => Roll {
                        x: roll.x + 1,
                        y: roll.y + 1,
                    },
                    Direction::S => Roll {
                        x: roll.x,
                        y: roll.y + 1,
                    },
                    Direction::SW => Roll {
                        x: roll.x - 1,
                        y: roll.y + 1,
                    },
                    Direction::W => Roll {
                        x: roll.x - 1,
                        y: roll.y,
                    },
                    Direction::NW => Roll {
                        x: roll.x - 1,
                        y: roll.y - 1,
                    },
                };
                n += if rolls_of_paper.contains(&next) { 1 } else { 0 };
            });
            if n < 4 {
                to_remove.insert(roll);
            }
        });
        if to_remove.is_empty() {
            break;
        }
    }
    total
}

enum Direction {
    N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW,
}

#[derive(Eq, PartialEq, Debug, Hash, Clone)]
struct Roll {
    x: i32,
    y: i32,
}
