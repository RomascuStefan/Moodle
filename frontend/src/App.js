import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import IndexPage from './components/index';
import DisciplinaPage from './components/disciplina';
import LoginPage from './components/LoginForm';
import YourDisciplinesPage from './components/yourDisciplinePage';
import UploadFilePage from './components/uploadFiles';
import CreateDisciplinaPage from './components/createDisciplinaPage';

const App = () => {
  return (
    <Router>
      <div>
        <header>
          <nav>
            <Link to="/" style={{ marginRight: '10px' }}>Home</Link>
          </nav>
        </header>
        <main>
          <Routes>
            <Route path="/" element={<IndexPage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/your-disciplines" element={<YourDisciplinesPage />} />
            <Route path="/disciplina/:cod" element={<DisciplinaPage />} />
            <Route path="/disciplina/:cod/upload" element={<UploadFilePage />} />
            <Route path="/create-disciplina" element={<CreateDisciplinaPage />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
};

export default App;
