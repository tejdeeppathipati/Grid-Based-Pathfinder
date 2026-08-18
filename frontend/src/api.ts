export type Position = { row: number; column: number }
export type Algorithm = 'BFS' | 'DIJKSTRA' | 'ASTAR'
export type SearchResult = {
  algorithm: Algorithm
  steps: number
  totalCost: number
  visitedCells: number
  durationNanos: number
  path: Position[]
}

export async function searchPath(grid: (number | null)[][], start: Position,
  destination: Position, algorithm: Algorithm): Promise<SearchResult> {
  const response = await fetch('/api/v1/paths/search', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ grid, start, destination, algorithm })
  })
  if (!response.ok) {
    const problem = await response.json().catch(() => ({}))
    throw new Error(problem.detail ?? `Search failed (${response.status})`)
  }
  return response.json()
}
