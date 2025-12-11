import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import DagCanvas from './components/canvas/DagCanvas';
import NodePropertyPanel from './components/nodes/NodePropertyPanel';
import { pipelineApi } from './api/pipelineApi';
import type { PipelineSpec, Node } from './types/dag';

const sampleSpec: PipelineSpec = {
  id: 'sample-1',
  name: 'Sample Pipeline',
  nodes: [
    {
      id: 'n1',
      type: 'sql',
      name: 'Extract Users',
      image: 'postgres-client:latest',
      command: ['psql', '-f', 'extract_users.sql'],
    },
    {
      id: 'n2',
      type: 'python',
      name: 'Transform Data',
      image: 'python:3.10',
      command: ['python', 'transform.py'],
    },
  ],
  edges: [{ from: 'n1', to: 'n2' }],
};

function App() {
  const [spec, setSpec] = useState<PipelineSpec>(sampleSpec);
  const [selectedNode, setSelectedNode] = useState<Node | null>(null);
  const [showPropertyPanel, setShowPropertyPanel] = useState(false);

  const handleSpecChange = (newSpec: PipelineSpec) => {
    setSpec(newSpec);
  };

  const handleNodeSelect = (node: Node) => {
    setSelectedNode(node);
    setShowPropertyPanel(true);
  };

  const handleNodeSave = (updatedNode: Node) => {
    const updatedNodes = spec.nodes.map((n) =>
      n.id === updatedNode.id ? updatedNode : n
    );
    setSpec({ ...spec, nodes: updatedNodes });
    setShowPropertyPanel(false);
    setSelectedNode(null);
  };

  return (
    <div className="h-screen w-screen flex flex-col bg-slate-50 dark:bg-slate-900">
      <header className="h-16 bg-white dark:bg-slate-800 border-b border-slate-200 dark:border-slate-700 flex items-center px-6 shadow-sm">
        <h1 className="text-2xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">
          Tantra
        </h1>
        <div className="ml-auto flex gap-2">
          <button className="px-4 py-2 bg-slate-100 hover:bg-slate-200 dark:bg-slate-700 dark:hover:bg-slate-600 text-slate-900 dark:text-slate-100 rounded-md transition-colors">
            Save
          </button>
          <button className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-md transition-colors">
            Run
          </button>
        </div>
      </header>
      <div className="flex-1 relative">
        <DagCanvas spec={spec} onSpecChange={handleSpecChange} />
        {showPropertyPanel && (
          <NodePropertyPanel
            node={selectedNode}
            onSave={handleNodeSave}
            onClose={() => {
              setShowPropertyPanel(false);
              setSelectedNode(null);
            }}
          />
        )}
      </div>
    </div>
  );
}

export default App;

