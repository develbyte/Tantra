import React, { useCallback, useMemo } from 'react';
import {
  ReactFlow,
  Background,
  Controls,
  MiniMap,
  Node,
  Edge,
  addEdge,
  Connection,
  useNodesState,
  useEdgesState,
  Panel,
} from '@xyflow/react';
import '@xyflow/react/dist/style.css';
import { motion } from 'framer-motion';
import type { PipelineSpec } from '../../types/dag';
import CustomNode from '../nodes/CustomNode';

const nodeTypes = {
  custom: CustomNode,
};

interface DagCanvasProps {
  spec: PipelineSpec;
  onSpecChange: (spec: PipelineSpec) => void;
}

const DagCanvas: React.FC<DagCanvasProps> = ({ spec, onSpecChange }) => {
  const initialNodes: Node[] = useMemo(() => 
    spec.nodes.map((node) => ({
      id: node.id,
      type: 'custom',
      position: { x: Math.random() * 400, y: Math.random() * 400 },
      data: { label: node.name, nodeType: node.type, ...node },
    }))
  , [spec.nodes]);

  const initialEdges: Edge[] = useMemo(() =>
    spec.edges.map((edge) => ({
      id: `${edge.from}-${edge.to}`,
      source: edge.from,
      target: edge.to,
      animated: true,
    }))
  , [spec.edges]);

  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);

  const onConnect = useCallback(
    (params: Connection) => {
      setEdges((eds) => addEdge(params, eds));
      // Update spec
      const newEdge = { from: params.source!, to: params.target! };
      onSpecChange({ ...spec, edges: [...spec.edges, newEdge] });
    },
    [spec, setEdges, onSpecChange]
  );

  return (
    <div className="w-full h-full bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800">
      <ReactFlow
        nodes={nodes}
        edges={edges}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        onConnect={onConnect}
        nodeTypes={nodeTypes}
        fitView
        className="bg-transparent"
      >
        <Background variant="dots" gap={20} size={1} />
        <Controls className="bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700" />
        <MiniMap
          className="bg-white dark:bg-slate-800 border border-slate-200 dark:border-slate-700"
          nodeColor={(node) => {
            const type = node.data?.nodeType;
            const colors: Record<string, string> = {
              task: '#3b82f6',
              sql: '#10b981',
              python: '#f59e0b',
              sensor: '#8b5cf6',
            };
            return colors[type] || '#6b7280';
          }}
        />
        <Panel position="top-left" className="m-4">
          <motion.div
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            className="bg-white/80 dark:bg-slate-800/80 backdrop-blur-sm rounded-lg shadow-lg p-4 border border-slate-200 dark:border-slate-700"
          >
            <h2 className="text-lg font-semibold text-slate-900 dark:text-slate-100">
              {spec.name}
            </h2>
            <p className="text-sm text-slate-600 dark:text-slate-400">
              {nodes.length} nodes • {edges.length} edges
            </p>
          </motion.div>
        </Panel>
      </ReactFlow>
    </div>
  );
};

export default DagCanvas;

