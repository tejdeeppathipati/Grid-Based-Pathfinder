import { useMemo, useState } from 'react'
import { Algorithm, Position, SearchResult, searchPath } from './api'

const SIZE = 10
const newGrid = () => Array.from({ length: SIZE }, () => Array<number | null>(SIZE).fill(1))
type Tool = 'wall' | 'weight' | 'start' | 'destination'

export default function App() {
  const [grid, setGrid] = useState(newGrid)
  const [start, setStart] = useState<Position>({ row: 1, column: 1 })
  const [destination, setDestination] = useState<Position>({ row: 8, column: 8 })
  const [algorithm, setAlgorithm] = useState<Algorithm>('ASTAR')
  const [tool, setTool] = useState<Tool>('wall')
  const [weight, setWeight] = useState(5)
  const [result, setResult] = useState<SearchResult | null>(null)
  const [error, setError] = useState('')
  const [running, setRunning] = useState(false)
  const pathKeys = useMemo(() => new Set(result?.path.map(p => `${p.row}:${p.column}`)), [result])

  function edit(row: number, column: number) {
    setResult(null); setError('')
    if (tool === 'start') return setStart({ row, column })
    if (tool === 'destination') return setDestination({ row, column })
    setGrid(current => current.map((cells, r) => cells.map((value, c) => {
      if (r !== row || c !== column) return value
      return tool === 'wall' ? (value === null ? 1 : null) : weight
    })))
  }

  async function run() {
    setRunning(true); setError(''); setResult(null)
    try { setResult(await searchPath(grid, start, destination, algorithm)) }
    catch (reason) { setError(reason instanceof Error ? reason.message : 'Search failed') }
    finally { setRunning(false) }
  }

  function reset() {
    setGrid(newGrid()); setResult(null); setError('')
    setStart({ row: 1, column: 1 }); setDestination({ row: 8, column: 8 })
  }

  return <main>
    <header>
      <p className="eyebrow">Interactive algorithm laboratory</p>
      <h1>Pathfinder <span>Lab</span></h1>
      <p>Design a weighted grid, compare search strategies, and inspect the route.</p>
    </header>

    <section className="workspace">
      <aside aria-label="Pathfinding controls">
        <label>Algorithm
          <select value={algorithm} onChange={event => setAlgorithm(event.target.value as Algorithm)}>
            <option value="BFS">BFS — fewest steps</option>
            <option value="DIJKSTRA">Dijkstra — lowest cost</option>
            <option value="ASTAR">A* — guided lowest cost</option>
          </select>
        </label>
        <fieldset><legend>Drawing tool</legend>
          {(['wall', 'weight', 'start', 'destination'] as Tool[]).map(value =>
            <button key={value} className={tool === value ? 'selected' : ''}
              onClick={() => setTool(value)}>{value}</button>)}
        </fieldset>
        <label>Cell weight
          <input type="number" min="1" max="99" value={weight}
            onChange={event => setWeight(Math.max(1, Number(event.target.value)))} />
        </label>
        <button className="run" onClick={run} disabled={running}>{running ? 'Searching…' : 'Run search'}</button>
        <button className="reset" onClick={reset}>Reset grid</button>
        {error && <p className="error" role="alert">{error}</p>}
        {result && <dl>
          <div><dt>Cost</dt><dd>{result.totalCost}</dd></div>
          <div><dt>Steps</dt><dd>{result.steps}</dd></div>
          <div><dt>Visited</dt><dd>{result.visitedCells}</dd></div>
          <div><dt>Runtime</dt><dd>{(result.durationNanos / 1_000_000).toFixed(3)} ms</dd></div>
        </dl>}
      </aside>

      <div className="board" role="grid" aria-label="Editable weighted grid">
        {grid.flatMap((row, r) => row.map((value, c) => {
          const isStart = start.row === r && start.column === c
          const isDestination = destination.row === r && destination.column === c
          const isPath = pathKeys.has(`${r}:${c}`)
          const className = ['cell', value === null && 'wall', isPath && 'path',
            isStart && 'start', isDestination && 'destination'].filter(Boolean).join(' ')
          return <button role="gridcell" aria-label={`row ${r}, column ${c}`}
            className={className} key={`${r}:${c}`} onClick={() => edit(r, c)}>
            {isStart ? 'S' : isDestination ? 'E' : value}
          </button>
        }))}
      </div>
    </section>
  </main>
}
