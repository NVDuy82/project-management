import React, { useState } from "react";
import {
  Box,
  Button, CircularProgress,
  IconButton,
  Menu, MenuItem,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow, Typography
} from "@mui/material";
import { styled } from "@mui/system";
import {FaCaretDown, FaEllipsisV, FaFilter, FaTimes} from "react-icons/fa";
import CustomMenuItem from "./CustumMenuItem";

const ButtonFilter = styled(Button)(({ theme, isFiltered }) => ({
  textTransform: "none",
  color: isFiltered ? "#005488" : "#686868",
  backgroundColor: isFiltered ? "#C2E7FF" : "#fff",
  border: isFiltered ? "1px solid #C2E7FF" : "1px solid #CECECE",
  borderRadius: isFiltered ? "24px 0px 0px 24px" : "24px",
  display: "inline-flex",
  alignItems: "center",
  fontFamily: "'Google Sans', sans-serif",
  "&:hover": {
    border: isFiltered ? "1px solid #C2E7FF" : "1px solid #686868",
    backgroundColor: isFiltered ? "#69c3ff" : "#f5f5f5", // Đổi màu khi hover nếu không được chọn
  }
}));

const StyledTableRow = styled(TableRow)(({ theme, selectedRowId, rowId }) => ({
  backgroundColor: selectedRowId === rowId ? "#5f83da" : "transparent",
  transition: "background-color 0.3s ease",
  "&:hover": {
    backgroundColor: selectedRowId !== rowId ? "#dcdcdc" : "",
  },
}));

const StyledHeadTableCell = styled(TableCell)(({ theme }) => ({
  fontFamily: "'Google Sans', sans-serif",
  fontWeight: "bold",
  cursor: "pointer",
}));

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  padding: "10px 8px",
  fontFamily: "'Google Sans', sans-serif",
  fontSize: "14px",
  textAlign: "left",
  "&:first-child": {
    fontWeight: "bold",
  },
  "&:last-child": {
    textAlign: "right", // Căn lề phải cho cột action
  },
}));

const ReusableTable = React.forwardRef((props, ref) => {
  const {
    columns,
    data,
    // selectedRowId,
    // onRowClick,
    onDoubleClickRow,
    onActionClick,
    loading,
    onScroll,
    showAction=true,
    sx
  } = props;
  const [selectedRowId, setSelectedRowId] = useState(null); // State cho selected row ID
  const [sortConfig, setSortConfig] = useState({ key: "", direction: "asc" });
  const [filter, setFilter] = useState({});
  const [anchorElFilter, setAnchorElFilter] = useState({});

  const handleClickRow = (e, rowId) => {
    setSelectedRowId(rowId); // Cập nhật selectedRowId khi click vào dòng
  };

  const handleCloseMenuFilter = (columnId) => {
    setAnchorElFilter((prevState) => ({
      ...prevState,
      [columnId]: null, // Đóng menu lọc cho cột tương ứng
    }));
  };

  const handleFilter = (columnId, value) => {
    setFilter((prevFilter) => ({
      ...prevFilter,
      [columnId]: value,
    }));
    handleCloseMenuFilter(columnId); // Đóng menu lọc sau khi chọn
  };

  const handleClearFilter = (column) => {
    setFilter((prevFilter) => {
      const updatedFilter = { ...prevFilter };
      delete updatedFilter[column]; // Xóa bộ lọc cho cột
      return updatedFilter;
    });
  };

  // Tạo danh sách các giá trị duy nhất cho mỗi cột
  const getUniqueValues = (columnId) => {
    const col = columns.find(col => col.id === columnId);
    return [...new Set(data.map((row) => col.render ? col.render(row) : row[col.id]))];
  };

  // Lọc dữ liệu dựa trên bộ lọc
  const filteredData = data.filter((row) => {
    // Kiểm tra tất cả các bộ lọc và lọc dữ liệu tương ứng
    return Object.keys(filter).every((columnId) => {
      if (filter[columnId]) {
        const col = columns.find(col => col.id === columnId);
        return (col.render ? col.render(row) : row[col.id]) === filter[columnId];
      }
      return true;
    });
  });

  // Hàm để sắp xếp dữ liệu
  const sortedData = [...filteredData].sort((a, b) => {
    if (!sortConfig.key) return 0;
    const column = columns.find(col => col.id === sortConfig.key);  // Lấy thông tin cột theo key
    const aValue = column.render ? column.render(a) : a[sortConfig.key];  // Nếu có render thì lấy giá trị render
    const bValue = column.render ? column.render(b) : b[sortConfig.key];  // Nếu có render thì lấy giá trị render

    if (aValue < bValue) {
      return sortConfig.direction === "asc" ? -1 : 1;
    }
    if (aValue > bValue) {
      return sortConfig.direction === "asc" ? 1 : -1;
    }
    return 0;
  });

  // Hàm để xử lý khi nhấn vào tiêu đề cột
  const handleSort = (columnKey) => {
    let direction = "asc";
    if (sortConfig.key === columnKey && sortConfig.direction === "asc") {
      direction = "desc";
    }
    setSortConfig({ key: columnKey, direction });
  };

  return (
    <Box>
      <Box sx={{ display: "flex", alignItems: "center", mb: 3 }}>
        <Box
          sx={{
            display: "inline-flex", // Đảm bảo icon hiển thị inline
            color: "gray", // Màu xám
            padding: "0px", // Khoảng cách giữa viền và biểu tượng
            alignItems: "center", // Căn chỉnh icon ở giữa
            mr: 2, // Thêm một khoảng cách giữa icon và button
          }}
        >
          <FaFilter /> {/* Biểu tượng lọc */}
        </Box>

        {/* Tự động render các nút lọc cho các cột có thể lọc */}
        {columns
          .filter((col) => col.filterable)
          .map((col) => (
            <Box sx={{ display: "flex", alignItems: "center", mr: 2 }} key={col.id}>
              <ButtonFilter
                onClick={(e) =>
                  setAnchorElFilter((prevState) => ({
                    ...prevState,
                    [col.id]: e.currentTarget,
                  }))
                }
                variant="outlined"
                isFiltered={Boolean(filter[col.id])}
              >
                {col.label}
                <FaCaretDown style={{ marginLeft: "8px", fontSize: "14px" }} />
              </ButtonFilter>

              {/* Nếu có bộ lọc cho cột này, hiển thị nút X */}
              {filter[col.id] && (
                <IconButton
                  onClick={() => handleClearFilter(col.id)}
                  sx={{
                    color: "#005488",
                    backgroundColor: "#C2E7FF",
                    border: "1px solid #C2E7FF",
                    borderRadius: "0px 24px 24px 0px",
                    display: "inline-flex",
                    alignItems: "center",
                    "&:hover": {
                      border: "1px solid #C2E7FF",
                      backgroundColor: "#69c3ff",
                    }
                  }}
                >
                  <FaTimes size={19} />
                </IconButton>
              )}
            </Box>
          ))}
      </Box>

      {/* Menu cho phép lọc theo cột cụ thể */}
      {columns
        .filter((col) => col.filterable)
        .map((col) => (
          <Menu
            key={col.id}
            anchorEl={anchorElFilter[col.id]}
            open={Boolean(anchorElFilter[col.id])}
            onClose={() => handleCloseMenuFilter(col.id)}
            anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
            transformOrigin={{ vertical: "top", horizontal: "left" }}
          >
            <Typography variant="body2" fontWeight="bold" sx={{ p: 1, fontFamily: "'Google Sans', sans-serif" }}>
              Lọc theo "{col.label}"
            </Typography>
            {getUniqueValues(col.id).map((value) => (
              <CustomMenuItem key={value} onClick={() => handleFilter(col.id, value)}>
                {value}
              </CustomMenuItem>
            ))}
          </Menu>
        ))}

      <TableContainer
        ref={ref}
        onScroll={onScroll}
        sx={{
          height: "calc(100vh - 255px)",
          overflowY: "auto",
          '&::-webkit-scrollbar': {
            width: '8px',
          },
          '&::-webkit-scrollbar-track': {
            backgroundColor: 'transparent',
          },
          '&::-webkit-scrollbar-thumb': {
            backgroundColor: '#888',
            borderRadius: '4px',
            transition: 'background-color 0.3s ease',
          },
          '&::-webkit-scrollbar-thumb:hover': {
            backgroundColor: '#555',
          },
          ...sx,  // Cho phép thêm các style tùy chỉnh từ bên ngoài
        }}
      >
        <Table>
          <TableHead
            sx={{
              position: "sticky", top: 0, backgroundColor: "#fff", zIndex: 1, borderBottom: "2px solid #ddd"
            }}
          >
            <TableRow>
              {columns.map((col) => (
                <StyledHeadTableCell
                  key={col.id}
                  onClick={() => handleSort(col.id)} // Thêm sự kiện sắp xếp vào tiêu đề cột
                >
                  {col.label}
                  {sortConfig.key === col.id ? (
                    sortConfig.direction === "asc" ? " ↑" : " ↓"
                  ) : null}
                </StyledHeadTableCell>
              ))}
              <StyledHeadTableCell />
            </TableRow>
          </TableHead>
          <TableBody>
            {sortedData.map((row) => (
              <StyledTableRow
                key={row.id}
                selectedRowId={selectedRowId}
                rowId={row.id}
                onClick={(e) => handleClickRow(e, row.id)}
                onDoubleClick={(e) => onDoubleClickRow && onDoubleClickRow(e, row)}
              >
                {columns.map((col) => (
                  <StyledTableCell key={col.id}>
                    {col.render ? col.render(row) : row[col.id]}  {/* Render nếu có hàm render */}
                  </StyledTableCell>
                ))}

                <StyledTableCell>
                  <IconButton
                    onClick={(e) => onActionClick && onActionClick(e, row)}
                    disabled={!showAction}
                    sx={{ fontSize: "16px", padding: "8px" }}
                  >
                    <FaEllipsisV />
                  </IconButton>
                </StyledTableCell>
              </StyledTableRow>
            ))}

            {/* Row loading */}
            {loading && (
              <TableRow>
                <StyledTableCell colSpan={columns.length + 1} sx={{ textAlign: "center" }}>
                  <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center" }}>
                    <CircularProgress />
                  </Box>
                </StyledTableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
});

export default ReusableTable;
