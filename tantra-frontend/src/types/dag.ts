export interface PipelineSpec {
  id: string;
  name: string;
  version?: string;
  params?: Record<string, ParamDefinition>;
  nodes: Node[];
  edges: Edge[];
  schedule?: Schedule;
  secrets?: SecretReference[];
}

export interface ParamDefinition {
  type: 'string' | 'number' | 'boolean';
  default?: string | number | boolean;
}

export interface Node {
  id: string;
  type: NodeType;
  name: string;
  image?: string;
  command?: string[];
  inputs?: string[];
  outputs?: string[];
  resources?: Resources;
  retries?: number;
  timeout_seconds?: number;
}

export type NodeType = 'task' | 'sensor' | 'branch' | 'map' | 'sql' | 'python';

export interface Resources {
  cpu?: string;
  memory?: string;
}

export interface Edge {
  from: string;
  to: string;
}

export interface Schedule {
  cron: string;
}

export interface SecretReference {
  ref: string;
  target: string;
}

export interface Pipeline {
  id: string;
  name: string;
  version: number;
  spec: PipelineSpec;
  createdBy?: string;
  createdAt: string;
  updatedAt: string;
  versions?: PipelineVersion[];
}

export interface PipelineVersion {
  id: string;
  version: number;
  gitCommitSha?: string;
  createdBy?: string;
  createdAt: string;
}

export interface Run {
  runId: string;
  pipelineId: string;
  version: number;
  status: RunStatus;
  startedAt?: string;
  completedAt?: string;
  triggeredBy?: string;
}

export type RunStatus = 'PENDING' | 'RUNNING' | 'SUCCESS' | 'FAILED' | 'CANCELLED';

