import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import IndexPage from './components/index';
import DisciplinaPage from './components/disciplina';
import LoginPage from './components/LoginForm';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<IndexPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/disciplina/:cod" element={<DisciplinaPage />} />
      </Routes>
    </Router>
  );
};

export default App;

