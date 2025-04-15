import React, { useState } from 'react';
import '../style/ExportMatches.css';

const ExportMatches = () => {
  const [format, setFormat] = useState('csv');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');

  const handleExport = async () => {
    try {
        let url = `http://localhost:8080/api/matches/export?format=${format}`;
        if (startDate) url += `&fromDate=${startDate}`;
        if (endDate) url += `&toDate=${endDate}`;

      const response = await fetch(url, {
        method: 'GET',
      });

      if (!response.ok) throw new Error('Failed to export');

      const blob = await response.blob();
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = `matches_export.${format}`;
      link.click();
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <div className="export-matches-container">
      <h2>Export Matches</h2>

      <label>
        Format:
        <select value={format} onChange={(e) => setFormat(e.target.value)}>
          <option value="csv">CSV</option>
          <option value="json">JSON</option>
          <option value="txt">TXT</option> 

        </select>
      </label>

      <label>
        Start Date:
        <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />
      </label>

      <label>
        End Date:
        <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} />
      </label>

      <button className="export-btn" onClick={handleExport}>Download</button>
    </div>
  );
};

export default ExportMatches;
