import React from 'react';
import { Handle, Position, NodeProps } from '@xyflow/react';
import { motion } from 'framer-motion';
import type { NodeType } from '../../types/dag';

interface CustomNodeData {
  label: string;
  nodeType: NodeType;
}

const nodeTypeColors: Record<NodeType, string> = {
  task: 'bg-blue-500',
  sql: 'bg-green-500',
  python: 'bg-yellow-500',
  sensor: 'bg-purple-500',
  branch: 'bg-pink-500',
  map: 'bg-indigo-500',
};

const CustomNode: React.FC<NodeProps<CustomNodeData>> = ({ data, selected }) => {
  return (
    <motion.div
      initial={{ scale: 0.8, opacity: 0 }}
      animate={{ scale: 1, opacity: 1 }}
      className={`px-4 py-3 rounded-lg shadow-lg border-2 ${
        selected
          ? 'border-blue-500 shadow-blue-500/50'
          : 'border-slate-300 dark:border-slate-600'
      } bg-white dark:bg-slate-800 backdrop-blur-sm`}
    >
      <Handle type="target" position={Position.Top} className="w-3 h-3" />
      <div className="flex items-center gap-2">
        <div className={`w-3 h-3 rounded-full ${nodeTypeColors[data.nodeType] || 'bg-gray-500'}`} />
        <div>
          <div className="font-semibold text-slate-900 dark:text-slate-100 text-sm">
            {data.label}
          </div>
          <div className="text-xs text-slate-500 dark:text-slate-400 uppercase">
            {data.nodeType}
          </div>
        </div>
      </div>
      <Handle type="source" position={Position.Bottom} className="w-3 h-3" />
    </motion.div>
  );
};

export default CustomNode;

