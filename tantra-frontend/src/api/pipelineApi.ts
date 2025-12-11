import axios from 'axios';
import type { Pipeline, PipelineSpec, Run, RunRequest } from '../types/dag';

const api = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add user ID header (in real app, get from auth context)
api.interceptors.request.use((config) => {
  const userId = localStorage.getItem('userId') || 'system';
  config.headers['X-User-Id'] = userId;
  return config;
});

export const pipelineApi = {
  create: async (name: string, spec: PipelineSpec): Promise<Pipeline> => {
    const response = await api.post<Pipeline>('/pipelines', { name, spec });
    return response.data;
  },

  get: async (id: string): Promise<Pipeline> => {
    const response = await api.get<Pipeline>(`/pipelines/${id}`);
    return response.data;
  },

  update: async (id: string, name: string, spec: PipelineSpec): Promise<Pipeline> => {
    const response = await api.put<Pipeline>(`/pipelines/${id}`, { name, spec });
    return response.data;
  },

  delete: async (id: string): Promise<void> => {
    await api.delete(`/pipelines/${id}`);
  },

  validate: async (id: string): Promise<{ valid: boolean; errors: string[]; warnings: string[] }> => {
    const response = await api.post(`/pipelines/${id}/validate`);
    return response.data;
  },

  getVersions: async (id: string): Promise<Pipeline> => {
    const response = await api.get<Pipeline>(`/pipelines/${id}/versions`);
    return response.data;
  },
};

export const runApi = {
  trigger: async (pipelineId: string, request: RunRequest): Promise<Run> => {
    const response = await api.post<Run>(`/runs/pipelines/${pipelineId}/run`, request);
    return response.data;
  },

  get: async (runId: string): Promise<Run> => {
    const response = await api.get<Run>(`/runs/${runId}`);
    return response.data;
  },

  list: async (pipelineId?: string, status?: string): Promise<Run[]> => {
    const params = new URLSearchParams();
    if (pipelineId) params.append('pipelineId', pipelineId);
    if (status) params.append('status', status);
    const response = await api.get<Run[]>(`/runs?${params.toString()}`);
    return response.data;
  },
};

export const gitApi = {
  commit: async (pipelineId: string, message: string): Promise<{ commitSha: string; message: string }> => {
    const response = await api.post('/git/commit', { pipelineId, message });
    return response.data;
  },

  getVersions: async (pipelineId: string) => {
    const response = await api.get(`/git/versions/${pipelineId}`);
    return response.data;
  },

  getDiff: async (pipelineId: string, fromVersion: number, toVersion: number) => {
    const response = await api.post('/git/diff', { pipelineId, fromVersion, toVersion });
    return response.data;
  },

  rollback: async (pipelineId: string, version: number) => {
    const response = await api.post('/git/rollback', { pipelineId, version });
    return response.data;
  },
};

