import { afterEach, describe, expect, it, vi } from 'vitest'
import { searchPath } from './api'

describe('searchPath', () => {
  afterEach(() => vi.restoreAllMocks())

  it('posts a pathfinding request and returns the result', async () => {
    const result = { algorithm: 'ASTAR', steps: 1, totalCost: 1,
      visitedCells: 2, durationNanos: 10, path: [{ row: 0, column: 0 }, { row: 0, column: 1 }] }
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue({ ok: true, json: async () => result }))

    await expect(searchPath([[1, 1]], { row: 0, column: 0 },
      { row: 0, column: 1 }, 'ASTAR')).resolves.toEqual(result)
    expect(fetch).toHaveBeenCalledWith('/api/v1/paths/search', expect.objectContaining({ method: 'POST' }))
  })

  it('surfaces Problem Details messages', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue({
      ok: false, status: 422, json: async () => ({ detail: 'No path connects the cells' })
    }))

    await expect(searchPath([[1]], { row: 0, column: 0 },
      { row: 0, column: 0 }, 'BFS')).rejects.toThrow('No path connects the cells')
  })
})
